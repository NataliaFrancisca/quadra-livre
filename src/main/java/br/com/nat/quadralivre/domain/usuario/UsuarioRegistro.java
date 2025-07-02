package br.com.nat.quadralivre.domain.usuario;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRegistro(
        @NotBlank
        @Email(message = "Digite um e-mail válido. Exemplo: email@email.com")
        String login,
        @NotBlank
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres." )
        String senha,
        @NotBlank(message = "O campo nome é obrigatório.")
        String nome,
        @NotBlank(message = "O campo telefone é obrigatório.")
        String telefone,
        @NotBlank
        @CPF(message = "Digite um número de CPF válido.")
        String cpf,
        @NotNull(message = "Digite um valor válido. Ex.: 'GESTOR' ou 'USUARIO'.")
        Role role
) {
}
