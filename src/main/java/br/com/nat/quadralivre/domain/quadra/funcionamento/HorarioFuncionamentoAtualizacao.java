package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioFuncionamentoAtualizacao(
        @NotNull(message = "O campo dia da semana é obrigatório. ")
        DiaSemana diaSemana,
        LocalTime abertura,
        LocalTime fechamento,
        Boolean disponibilidade
) {
}
