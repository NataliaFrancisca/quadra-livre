package br.com.nat.quadralivre.domain.usuario;

public record UsuarioAtualizacao(
        String senha,
        String nome,
        String telefone
) {
}
