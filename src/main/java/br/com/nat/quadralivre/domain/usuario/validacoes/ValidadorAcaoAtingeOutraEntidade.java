package br.com.nat.quadralivre.domain.usuario.validacoes;

import br.com.nat.quadralivre.domain.quadra.QuadraRepository;
import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import br.com.nat.quadralivre.domain.usuario.Role;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAcaoAtingeOutraEntidade {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private QuadraRepository quadraRepository;

    private void validarSeTemReservas(String email){
        var usuarioTemReservas = this.reservaRepository.existsByUsuarioEmail(email);

        if (usuarioTemReservas){
            throw new IllegalArgumentException("Ação não pode ser concluída porque usuário tem reservas ativas.");
        }
    }

    private void validarSeTemQuadras(String email){
        var usuarioTemQuadras = this.quadraRepository.existsByGestorEmail(email);

        if (usuarioTemQuadras){
            throw new IllegalArgumentException("Ação não pode ser concluída porque gestor tem quadras ativas.");
        }
    }

    public void validar(Usuario usuario){

        if (usuario.getRole().equals(Role.USUARIO)){
            this.validarSeTemReservas(usuario.getEmail());
        }

        if (usuario.getRole().equals(Role.GESTOR)){
            this.validarSeTemQuadras(usuario.getEmail());
        }
    }
}
