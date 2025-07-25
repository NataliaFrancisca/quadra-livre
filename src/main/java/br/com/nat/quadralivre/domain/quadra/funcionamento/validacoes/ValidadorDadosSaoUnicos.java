package br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes;

import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component("validadorDadosSaoUnicoHorarioFuncionamento")
public class ValidadorDadosSaoUnicos {
    @Autowired
    private HorarioFuncionamentoRepository repository;

    public void validar(DiaSemana diaSemana, Long quadraId){
        var diaSemanaJaCadastrado = this.repository.existsByDiaAndQuadraId(diaSemana, quadraId);

        if (diaSemanaJaCadastrado){
            throw new DataIntegrityViolationException("Já existe um cadastro para esse dia da semana: " + diaSemana.name());
        }
    }
}
