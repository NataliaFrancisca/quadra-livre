package br.com.nat.quadralivre.domain.quadra.validacoes;

import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("validadorAcaoAtingeOutraEntidadeQuadra")
public class ValidadorAcaoAtingeOutraEntidade {

    @Autowired
    private ReservaRepository repository;

    public void validar(Long quadraId){
        var existeReservasParaQuadra = this.repository.existsByQuadraId(quadraId);

        if (existeReservasParaQuadra){
            throw new IllegalArgumentException("Ação não pode ser concluída porque existe reservas para essa quadra.");
        }
    }
}
