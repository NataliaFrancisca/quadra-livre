package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import br.com.nat.quadralivre.domain.usuario.UsuarioDadosAberto;

public record QuadraDadosDetalhados(
        Long id,
        String nome,
        Endereco endereco,
        Integer minutosReserva,
        Integer minutosIntervalo,
        UsuarioDadosAberto gestor
) {
    public QuadraDadosDetalhados(Quadra quadra){
        this(quadra.getId(), quadra.getNome(), quadra.getEndereco(), quadra.getMinutosReserva(), quadra.getMinutosIntervalo(), new UsuarioDadosAberto(quadra.getGestor()));
    }
}
