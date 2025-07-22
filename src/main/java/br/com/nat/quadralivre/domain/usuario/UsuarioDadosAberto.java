package br.com.nat.quadralivre.domain.usuario;

public record UsuarioDadosAberto(
        String email,
        String nome,
        String telefone
) {
public UsuarioDadosAberto(Usuario usuario){
        this(usuario.getEmail(), usuario.getNome(), usuario.getTelefone());
    }
}
