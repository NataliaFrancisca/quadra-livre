package br.com.nat.quadralivre.domain.quadra.indisponibilidade;

import br.com.nat.quadralivre.domain.quadra.QuadraDadosAberto;

import java.time.LocalDate;

public record IndisponibilidadeDadosDetalhados(
        LocalDate data,
        QuadraDadosAberto quadra
) {
    public IndisponibilidadeDadosDetalhados(Indisponibilidade indisponibilidade){
        this(indisponibilidade.getData(), new QuadraDadosAberto(indisponibilidade.getQuadra()));
    }
}
