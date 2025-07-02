package br.com.nat.quadralivre.domain.quadra.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRegistro(
        @NotBlank
        @Size(min = 10)
        String local,
        @NotBlank
        @Size(min = 10)
        String rua,
        @NotBlank
        @Size(min = 10)
        String numero,
        @NotBlank
        @Size(min = 10)
        String bairro,
        @NotBlank
        @Size(min = 10)
        String cidade,
        @NotBlank
        @Size(min = 2, max = 2)
        Estado estado
) {
}
