package br.com.nat.quadralivre.domain.quadra.validacoes;

import br.com.nat.quadralivre.domain.quadra.QuadraRegistro;
import br.com.nat.quadralivre.domain.quadra.QuadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component("validadorDadosSaoUnicosQuadra")
public class ValidadorDadosSaoUnicos {

    @Autowired
    private QuadraRepository repository;

    public void validar(QuadraRegistro quadra){
        boolean quadraJaExiste = this.repository.existsByNomeAndEndereco(
                quadra.nome(),
                quadra.endereco());

        if (quadraJaExiste){
            throw new DataIntegrityViolationException("Já existe uma quadra cadastrada com esses dados.");
        }
    }
}
