package br.com.nat.quadralivre.domain.quadra.endereco;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRegistro(
        @NotBlank
        String local,
        @NotBlank
        String rua,
        @NotBlank
        String numero,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        Estado estado
) {
}
