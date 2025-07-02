package br.com.nat.quadralivre.domain.reserva;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaBusca(
        @NotNull
        Long quadraId,
        @NotNull(message = "O campo data é obrigatório.")
        @FutureOrPresent(message = "A data deve ser no presente ou futuro.")
        LocalDate data
) {
}
