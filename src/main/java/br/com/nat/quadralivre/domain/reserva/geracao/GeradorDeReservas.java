package br.com.nat.quadralivre.domain.reserva.geracao;

import br.com.nat.quadralivre.domain.quadra.QuadraRepository;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import br.com.nat.quadralivre.domain.reserva.Reserva;
import br.com.nat.quadralivre.domain.reserva.ReservaDisponivel;
import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class GeradorDeReservas {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private QuadraRepository quadraRepository;

    private LocalDateTime horarioReservaDinamico;
    private LocalDateTime horarioReservasEncerramento;

    private HorarioFuncionamento horarioFuncionamento;

    private List<ReservaDisponivel> exibirSomenteReservasLivres(LocalDate dataSolicitada, List<ReservaDisponivel> reservasDisponiveis){
        List<Reserva> reservasJaReservadas = this.reservaRepository.findAllByQuadraIdAndDataIsBetween(
                this.horarioFuncionamento.getQuadra().getId(),
                dataSolicitada.atTime(this.horarioFuncionamento.getAbertura()),
                dataSolicitada.atTime(this.horarioFuncionamento.getFechamento())
        );

        return reservasDisponiveis
                .stream().filter(rd -> reservasJaReservadas.stream().noneMatch(rr -> rr.getData().isEqual(rd.getData()))).toList()
                .stream().filter(h -> h.getData().isAfter(LocalDateTime.now())).sorted(Comparator.comparing(ReservaDisponivel::getInicio)).toList();
    }

    private List<ReservaDisponivel> gerarReservasPorDuracaoPadrao(LocalDate dataSolicitada){
        List<ReservaDisponivel> reservas = new ArrayList<>();

        final var quadra = this.quadraRepository.getReferenceById(this.horarioFuncionamento.getQuadra().getId());
        final int minutosParaReserva = quadra.getMinutosReserva();
        final int minutosParaIntervalo = quadra.getMinutosIntervalo();

        while (this.horarioReservaDinamico.plusMinutes(minutosParaReserva).isBefore(this.horarioReservasEncerramento)){
            ReservaDisponivel reserva = new ReservaDisponivel(this.horarioFuncionamento, this.horarioReservaDinamico, dataSolicitada, minutosParaReserva);
            reservas.add(reserva);
            this.horarioReservaDinamico = this.horarioReservaDinamico.plusMinutes(minutosParaReserva).plusMinutes(minutosParaIntervalo);
        }

        return reservas;
    }

    private List<ReservaDisponivel> gerarReservasComplementares(LocalDate dataSolicitada){
        List<ReservaDisponivel> reservas = new ArrayList<>();
        final int HORAS_PARA_RESERVA_CURTA = 60;

        if (this.horarioReservaDinamico.plusMinutes(HORAS_PARA_RESERVA_CURTA).isBefore(this.horarioReservasEncerramento)){
            int diferencaEntreMinutos = (int) this.horarioReservaDinamico.until(this.horarioReservasEncerramento, ChronoUnit.MINUTES);
            ReservaDisponivel reserva = new ReservaDisponivel(this.horarioFuncionamento, this.horarioReservaDinamico, dataSolicitada, diferencaEntreMinutos);
            reservas.add(reserva);
        }

        return reservas;
    }

    public List<ReservaDisponivel> gerar(HorarioFuncionamento horarioFuncionamento, LocalDate dataSolicitada){
        this.horarioFuncionamento = horarioFuncionamento;
        this.horarioReservasEncerramento = LocalDateTime.of(dataSolicitada, horarioFuncionamento.getFechamento());
        this.horarioReservaDinamico = LocalDateTime.of(dataSolicitada, horarioFuncionamento.getAbertura());

        List<ReservaDisponivel> reservas = new ArrayList<>();

        reservas.addAll(this.gerarReservasPorDuracaoPadrao(dataSolicitada));
        reservas.addAll(this.gerarReservasComplementares(dataSolicitada));

        return this.exibirSomenteReservasLivres(dataSolicitada, reservas);
    }
}
