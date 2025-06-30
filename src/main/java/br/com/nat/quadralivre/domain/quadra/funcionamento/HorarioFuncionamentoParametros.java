package br.com.nat.quadralivre.domain.quadra.funcionamento;

import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.validation.constraints.NotNull;

public record HorarioFuncionamentoParametros(
        @NotNull
        Long quadraId,
        @NotNull
        Usuario gestor,
        @NotNull
        DiaSemana diaSemana
) {
}
