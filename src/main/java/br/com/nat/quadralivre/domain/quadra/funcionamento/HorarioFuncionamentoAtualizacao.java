package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record HorarioFuncionamentoAtualizacao(
        @NotNull
        DiaSemana diaSemana,
        LocalTime abertura,
        LocalTime fechamento,
        Boolean disponibilidade
) {
}
