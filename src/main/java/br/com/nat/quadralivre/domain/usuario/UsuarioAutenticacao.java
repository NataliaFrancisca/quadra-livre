package br.com.nat.quadralivre.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticacao(
        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}
