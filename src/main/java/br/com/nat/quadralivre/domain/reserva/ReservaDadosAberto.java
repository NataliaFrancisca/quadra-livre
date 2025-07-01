package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.QuadraDadosAberto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaDadosAberto(
        String id,
        LocalTime abertura,
        LocalTime fechamento,
        LocalDateTime data,
        QuadraDadosAberto quadra
) {
    public ReservaDadosAberto(Reserva reserva){
        this(
                reserva.getId(),
                reserva.getAbertura(),
                reserva.getEncerramento(),
                reserva.getData(),
                new QuadraDadosAberto(reserva.quadra)
        );
    }
}
