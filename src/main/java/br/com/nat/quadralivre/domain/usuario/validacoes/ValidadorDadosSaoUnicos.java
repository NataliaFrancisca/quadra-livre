package br.com.nat.quadralivre.domain.usuario.validacoes;

import br.com.nat.quadralivre.domain.usuario.UsuarioRegistro;
import br.com.nat.quadralivre.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDadosSaoUnicos {

    @Autowired
    private UsuarioRepository repository;

    public void validar(UsuarioRegistro usuario){
        var email = this.repository.existsByEmail(usuario.email());

        if (email){
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com esse e-mail.");
        }

        var cpf = this.repository.existsByCpf(usuario.cpf());

        if (cpf){
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com esse CPF.");
        }
    }
}
