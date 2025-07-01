package br.com.nat.quadralivre.domain.reserva;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaBusca(
        @NotNull
        Long quadraId,
        @FutureOrPresent
        LocalDate data
) {
}
