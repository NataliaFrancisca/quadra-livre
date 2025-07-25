package br.com.nat.quadralivre.domain.quadra.funcionamento;

import java.time.LocalTime;

public record HorarioFuncionamentoDadosDetalhados(
        Long id,
        DiaSemana diaSemana,
        LocalTime abertura,
        LocalTime fechamento,
        boolean disponibilidade
) {
    public HorarioFuncionamentoDadosDetalhados(HorarioFuncionamento funcionamento){
        this(funcionamento.getId(), funcionamento.getDia(), funcionamento.getAbertura(), funcionamento.getFechamento(), funcionamento.isDisponibilidade());
    }
}
