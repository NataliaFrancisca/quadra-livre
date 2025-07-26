package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.usuario.UsuarioDadosAberto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaDadosAberto(
        String id,
        LocalTime inicio,
        LocalTime encerramento,
        LocalDateTime data,
        String quadra,
        UsuarioDadosAberto usuario
) {
    public ReservaDadosAberto(Reserva reserva){
        this(
                reserva.getId(),
                reserva.getAbertura(),
                reserva.getEncerramento(),
                reserva.getData(),
                reserva.getQuadra().getNome(),
                new UsuarioDadosAberto(reserva.getUsuario())
        );
    }
}
