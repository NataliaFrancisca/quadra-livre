package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioFuncionamentoRegistro(
        @NotNull
        DiaSemana diaSemana,
        @NotNull
        LocalTime abertura,
        @NotNull
        LocalTime fechamento
) {
}
