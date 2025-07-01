package br.com.nat.quadralivre.domain.reserva;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaRegistro(
        @NotNull
        Long quadraId,
        @NotNull
        @FutureOrPresent
        LocalDate data,
        @NotNull
        String reservaId
) {
}
