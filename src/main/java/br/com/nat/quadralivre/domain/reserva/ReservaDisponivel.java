package br.com.nat.quadralivre.domain.reserva;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaDisponivel(
        String id,
        LocalTime abertura,
        LocalTime fechamento,
        LocalDateTime data
) {
}
