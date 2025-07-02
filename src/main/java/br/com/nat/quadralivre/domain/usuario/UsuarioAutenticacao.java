package br.com.nat.quadralivre.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAutenticacao(
        @NotBlank
        @Email(message = "Digite um e-mail válido. Exemplo: email@email.com")
        String login,
        @NotBlank
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres." )
        String senha
) {
}
