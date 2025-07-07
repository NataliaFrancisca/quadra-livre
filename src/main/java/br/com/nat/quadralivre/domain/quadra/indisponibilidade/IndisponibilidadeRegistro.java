package br.com.nat.quadralivre.domain.quadra.indisponibilidade;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record IndisponibilidadeRegistro(
        @NotNull
        @Future(message = "A data deve ser no futuro.")
        LocalDate data,
        @NotNull
        Long quadraId
) {
}
