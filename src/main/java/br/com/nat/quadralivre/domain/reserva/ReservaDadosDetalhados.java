package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.QuadraDadosAberto;
import br.com.nat.quadralivre.domain.usuario.UsuarioDadosAberto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaDadosDetalhados(
        String id,
        LocalTime abertura,
        LocalTime fechamento,
        LocalDateTime data,
        QuadraDadosAberto quadra,
        UsuarioDadosAberto usuario
) {
    public ReservaDadosDetalhados(Reserva reserva){
        this(
                reserva.getId(),
                reserva.getAbertura(),
                reserva.getEncerramento(),
                reserva.getData(),
                new QuadraDadosAberto(reserva.getQuadra()),
                new UsuarioDadosAberto(reserva.getUsuario())
        );
    }
}
