package br.com.nat.quadralivre.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRegistro(
        @NotBlank
        String login,
        @NotBlank
        String senha,
        @NotBlank
        String nome,
        @NotBlank
        String telefone,
        @NotBlank
        @CPF
        String cpf,
        @NotNull(message = "Digite um valor válido. Ex.: 'GESTOR' ou 'USUARIO'.")
        Role role
) {
}
