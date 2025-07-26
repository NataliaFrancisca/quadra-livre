package br.com.nat.quadralivre.domain.reserva.validacoes;

import br.com.nat.quadralivre.domain.reserva.Reserva;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("validadorUsuarioPodeRealizarAcaoReserva")
public class ValidadorUsuarioPodeRealizarAcao {

    public void validar(Reserva reserva, Usuario usuario){
        var usuarioReserva = reserva.getUsuario().getEmail();
        var gestorDaQuadra = reserva.getQuadra().getGestor().getEmail();

        if (!usuarioReserva.equals(usuario.getEmail())){
            if (!gestorDaQuadra.equals(usuario.getEmail())){
                throw new AccessDeniedException("Somente o usuário ou gestor responsável pela quadra pode acessar esses dados.");
            }
        }
    }
}
