package br.com.nat.quadralivre.domain.usuario.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAutenticacao(
        @NotBlank
        @Email(message = "Digite um e-mail válido. Exemplo: email@email.com")
        String email,
        @NotBlank
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres." )
        String senha
) {
}
