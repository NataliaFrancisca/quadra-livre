package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioFuncionamentoRegistro(
        @NotNull(message = "O campo dia da semana é obrigatório. ")
        DiaSemana diaSemana,
        @NotNull(message = "O campo horário de abertura é obrigatório.")
        LocalTime abertura,
        @NotNull(message = "O campo horário de fechamento é obrigatório.")
        LocalTime fechamento
) {
}
