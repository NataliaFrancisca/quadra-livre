package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuadraRegistro(
        @NotBlank
        String nome,
        @NotNull
        Endereco endereco
) {
}
