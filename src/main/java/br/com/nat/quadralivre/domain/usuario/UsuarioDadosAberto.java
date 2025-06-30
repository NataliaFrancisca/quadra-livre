package br.com.nat.quadralivre.domain.usuario;

public record UsuarioDadosAberto(
        String login,
        String nome,
        String telefone
) {
public UsuarioDadosAberto(Usuario usuario){
        this(usuario.getLogin(), usuario.getNome(), usuario.getTelefone());
    }
}
