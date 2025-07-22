package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import jakarta.validation.constraints.*;

public record QuadraRegistro(
        @NotBlank
        String nome,
        @NotNull
        Endereco endereco,
        @NotNull
        // min: 1 hora max: 12 horas
        @Min(60)
        @Max(720)
        Integer minutosReserva,
        @NotNull
        @PositiveOrZero
        Integer minutosIntervalo
) {
}
