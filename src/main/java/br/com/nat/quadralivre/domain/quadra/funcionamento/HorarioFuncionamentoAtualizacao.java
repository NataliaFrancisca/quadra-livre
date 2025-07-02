package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record HorarioFuncionamentoAtualizacao(
        @NotNull(message = "O campo dia da semana é obrigatório. ")
        DiaSemana diaSemana,
        @Size(min = 8, message = "O campo horário de abertura é obrigatório.")
        LocalTime abertura,
        @Size(min = 8, message = "O campo horário de fechamento é obrigatório.")
        LocalTime fechamento,
        Boolean disponibilidade
) {
}
