package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class ReservaDisponivel {
    private String id;
    private LocalTime inicio;
    private LocalTime encerramento;
    private LocalDateTime data;

    public ReservaDisponivel(HorarioFuncionamento horarioFuncionamento, LocalDateTime horarioReserva, LocalDate dataSolicitada, int minutosReserva){
        this.id = this.gerarHashComoString(horarioFuncionamento, horarioReserva);
        this.inicio = horarioReserva.toLocalTime();
        this.encerramento = horarioReserva.toLocalTime().plusMinutes(minutosReserva);
        this.data = dataSolicitada.atTime(horarioReserva.toLocalTime());
    }

    private String gerarHashComoString(HorarioFuncionamento funcionamentoDados, LocalDateTime horarioReserva){
        return UUID
                .nameUUIDFromBytes((
                        horarioReserva.toString() +
                                funcionamentoDados.getQuadra().getId() +
                                funcionamentoDados.getDia()).getBytes()
                ).toString();
    }
}
