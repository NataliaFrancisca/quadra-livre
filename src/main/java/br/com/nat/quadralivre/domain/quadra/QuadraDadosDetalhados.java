package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import br.com.nat.quadralivre.domain.usuario.UsuarioDadosAberto;

public record QuadraDadosDetalhados(
        Long id,
        String nome,
        Endereco endereco,
        UsuarioDadosAberto gestor
) {
    public QuadraDadosDetalhados(Quadra quadra){
        this(quadra.getId(), quadra.getNome(), quadra.getEndereco(), new UsuarioDadosAberto(quadra.getGestor()));
    }
}
