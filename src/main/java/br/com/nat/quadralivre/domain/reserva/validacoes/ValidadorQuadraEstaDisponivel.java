package br.com.nat.quadralivre.domain.reserva.validacoes;

import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeRepository;
import br.com.nat.quadralivre.domain.reserva.ReservaPesquisa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorQuadraEstaDisponivel {

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private IndisponibilidadeRepository indisponibilidadeRepository;

    public void validar(ReservaPesquisa reserva, HorarioFuncionamento horarioFuncionamento){
        var existeIndisponibiliade = this.indisponibilidadeRepository.existsByDataAndQuadraId(reserva.data(), reserva.quadraId());

        if (!horarioFuncionamento.isDisponibilidade() || existeIndisponibiliade){
            throw new IllegalArgumentException("A quadra escolhida não está disponível na data indicada.");
        }
    }
}
