package br.com.nat.quadralivre.domain.reserva.validacoes;

import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component("validadorDadosSaoUnicosReserva")
public class ValidadorDadosSaoUnicos {

    @Autowired
    private ReservaRepository reservaRepository;

    public void validar(String reservaID){
        boolean reservaExiste = this.reservaRepository.existsById(reservaID);

        if (reservaExiste){
            throw new DataIntegrityViolationException("Já existe uma reserva para a data e horário indicado.");
        }
    }
}
