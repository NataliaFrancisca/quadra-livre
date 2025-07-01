package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class GerarReservas {

    @Autowired
    private ReservaRepository reservaRepository;

    private final int MINUTOS_ENTRE_RESERVA = 10;

    private LocalDateTime horarioReservaDinamico;
    private LocalDateTime horarioReservasEncerramento;

    private String gerarHashComoString(LocalDateTime abertura, Quadra quadra, DiaSemana diaSemana){
        return UUID.nameUUIDFromBytes((abertura.toString() + quadra.getId() + diaSemana).getBytes()).toString();
    }

    private List<ReservaDisponivel> exibirSomenteReservasLivres(Long quadraId, LocalDateTime dataAbertura, LocalDateTime dataEncerramento, List<ReservaDisponivel> reservasDisponiveis){
        List<Reserva> reservasJaReservadas = this.reservaRepository.findAllByQuadraIdAndDataIsBetween(quadraId, dataAbertura, dataEncerramento);

        return reservasDisponiveis.stream()
                .filter(rd -> reservasJaReservadas.stream().noneMatch(rr -> rr.getData().isEqual(rd.data())))
                .toList();
    }

    private List<ReservaDisponivel> gerarReservasParaReservasLongas(HorarioFuncionamento funcionamentoDados, LocalDate dataSolicitada){
        List<ReservaDisponivel> reservas = new ArrayList<>();

        final int MINUTOS_PARA_RESERVA_LONGA = 120;

        while (this.horarioReservaDinamico.plusMinutes(MINUTOS_PARA_RESERVA_LONGA).isBefore(this.horarioReservasEncerramento)){
            var id = this.gerarHashComoString(
                    this.horarioReservaDinamico,
                    funcionamentoDados.getQuadra(),
                    funcionamentoDados.getDia()
            );

            ReservaDisponivel reserva = new ReservaDisponivel(
                    id,
                    this.horarioReservaDinamico.toLocalTime(),
                    this.horarioReservaDinamico.toLocalTime().plusMinutes(MINUTOS_PARA_RESERVA_LONGA),
                    dataSolicitada.atTime(this.horarioReservaDinamico.toLocalTime())
            );

            reservas.add(reserva);
            this.horarioReservaDinamico = this.horarioReservaDinamico.plusMinutes(MINUTOS_PARA_RESERVA_LONGA).plusMinutes(this.MINUTOS_ENTRE_RESERVA);
        }

        return reservas;
    }

    private List<ReservaDisponivel> gerarReservasParaReservasCurtas(HorarioFuncionamento horarioFuncionamentoDadosDetalhados, LocalDate dataSolicitada){
        List<ReservaDisponivel> reservas = new ArrayList<>();

        final int HORAS_PARA_RESERVA_CURTA = 60;

        if (this.horarioReservaDinamico.plusMinutes(HORAS_PARA_RESERVA_CURTA).isBefore(this.horarioReservasEncerramento)){
            long diferencaEntreMinutos = this.horarioReservaDinamico.until(this.horarioReservasEncerramento, ChronoUnit.MINUTES);

            var id = this.gerarHashComoString(
                    this.horarioReservaDinamico,
                    horarioFuncionamentoDadosDetalhados.getQuadra(),
                    horarioFuncionamentoDadosDetalhados.getDia()
            );

            ReservaDisponivel reserva = new ReservaDisponivel(
                    id,
                    this.horarioReservaDinamico.toLocalTime(),
                    this.horarioReservaDinamico.toLocalTime().plusMinutes(diferencaEntreMinutos),
                    dataSolicitada.atTime(this.horarioReservaDinamico.toLocalTime())
            );

            reservas.add(reserva);
        }

        return reservas;
    }

    public List<ReservaDisponivel> gerar(HorarioFuncionamento horarioFuncionamentoDadosDetalhados, LocalDate dataSolicitada){
        this.horarioReservasEncerramento = LocalDateTime.of(dataSolicitada, horarioFuncionamentoDadosDetalhados.getFechamento());
        this.horarioReservaDinamico = LocalDateTime.of(dataSolicitada, horarioFuncionamentoDadosDetalhados.getAbertura());

        var reservasLongas = this.gerarReservasParaReservasLongas(horarioFuncionamentoDadosDetalhados, dataSolicitada);
        var reservasCurtas = this.gerarReservasParaReservasCurtas(horarioFuncionamentoDadosDetalhados, dataSolicitada);

        List<ReservaDisponivel> reservas = new ArrayList<>();

        reservas.addAll(reservasLongas);
        reservas.addAll(reservasCurtas);

        reservas.sort(Comparator.comparing(ReservaDisponivel::abertura));

        var dataIndicadaMaisAbertura = dataSolicitada.atTime(horarioFuncionamentoDadosDetalhados.getAbertura());
        var dataIndicadaMaisEncerramento = dataSolicitada.atTime(horarioFuncionamentoDadosDetalhados.getFechamento());

        var quadraId = horarioFuncionamentoDadosDetalhados.getQuadra().getId();

        return this.exibirSomenteReservasLivres(
                quadraId,
                dataIndicadaMaisAbertura,
                dataIndicadaMaisEncerramento,
                reservas);
    }
}
