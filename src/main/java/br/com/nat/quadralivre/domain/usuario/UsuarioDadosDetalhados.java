package br.com.nat.quadralivre.domain.usuario;

public record UsuarioDadosDetalhados (
    Long id,
    String email,
    String nome,
    String telefone,
    String cpf,
    Role role
){
    public UsuarioDadosDetalhados(Usuario usuario){
        this(usuario.getId(), usuario.getEmail(), usuario.getNome(), usuario.getTelefone(), usuario.getCpf(), usuario.getRole());
    }
}
