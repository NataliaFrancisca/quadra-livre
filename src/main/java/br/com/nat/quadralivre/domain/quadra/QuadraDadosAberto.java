package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import br.com.nat.quadralivre.domain.usuario.UsuarioDadosAberto;

public record QuadraDadosAberto(
        String local,
        Endereco endereco,
        UsuarioDadosAberto gestor
) {
    public QuadraDadosAberto(Quadra quadra){
        this(quadra.getNome(), quadra.getEndereco(), new UsuarioDadosAberto(quadra.getGestor()));
    }
}
