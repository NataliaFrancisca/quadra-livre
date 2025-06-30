package br.com.nat.quadralivre.domain.quadra.funcionamento;

import java.time.LocalTime;

public record HorarioFuncionamentoDadosDetalhados(
        DiaSemana diaSemana,
        LocalTime abertura,
        LocalTime fechamento,
        boolean disponibilidade
) {
    public HorarioFuncionamentoDadosDetalhados(HorarioFuncionamento quadra){
        this(quadra.getDia(), quadra.getAbertura(), quadra.getFechamento(), quadra.isDisponibilidade());
    }
}
