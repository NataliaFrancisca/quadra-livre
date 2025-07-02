package br.com.nat.quadralivre.domain.usuario;

import jakarta.validation.constraints.Size;

public record UsuarioAtualizacao(
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
        String senha,
        @Size(min = 4, message = "O nome deve ter pelo menos 4 caracteres.")
        String nome,
        @Size(min = 14, message = "O telefone deve ter pelo menos 14 caracteres. Exemplo: (dd) xxxx-xxxx")
        String telefone
) {
}
