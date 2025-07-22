package br.com.nat.quadralivre.domain.quadra.validacoes;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("validadorUsuarioPodeRealizarAcaoQuadra")
public class ValidadorUsuarioPodeRealizarAcao {

    public void validar(Quadra quadra, Usuario usuarioRequisicao){

        if (!quadra.getGestor().getEmail().equals(usuarioRequisicao.getEmail())){
            throw new AccessDeniedException("Apenas o proprietário dos dados tem permissão de acesso.");
        }

    }
}
