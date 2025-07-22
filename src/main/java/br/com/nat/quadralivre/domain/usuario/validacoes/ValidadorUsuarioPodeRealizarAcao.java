package br.com.nat.quadralivre.domain.usuario.validacoes;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorUsuarioPodeRealizarAcao {

    public void validar(Long idUsuarioReq, Long idUsuario){
        if (!idUsuarioReq.equals(idUsuario)){
            throw new AccessDeniedException("Apenas o proprietário dos dados tem permissão de acesso.");
        }
    }
}
