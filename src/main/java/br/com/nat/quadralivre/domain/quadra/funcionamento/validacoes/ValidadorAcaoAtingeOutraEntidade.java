package br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes;

import br.com.nat.quadralivre.domain.quadra.QuadraRepository;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoAtualizacao;
import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component("validadorAcaoAntigeOutraEntidadeFuncionamento")
public class ValidadorAcaoAtingeOutraEntidade {

    @Autowired
    private QuadraRepository quadraRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    public void validar(Long quadraId, DiaSemana diaSemana){
        var existeReservasNoDiaSemana = this.reservaRepository
                .existsByQuadraIdAndDiaSemana(quadraId, diaSemana);

        if (existeReservasNoDiaSemana){
            throw new IllegalArgumentException("Ação não pode ser concluída porque existe reservas para a data indicada.");
        }
    }

    public void validar(HorarioFuncionamentoAtualizacao horarioFuncionamentoAtualizacao, HorarioFuncionamento horarioFuncionamento){
        var quadra = horarioFuncionamento.getQuadra();

        var abertura = Optional.ofNullable(horarioFuncionamentoAtualizacao.abertura()).orElse(horarioFuncionamento.getAbertura());
        var fechamento = Optional.ofNullable(horarioFuncionamentoAtualizacao.abertura()).orElse(horarioFuncionamento.getAbertura());

        var existeReservasNoDiaSemana = this.reservaRepository.findAllByQuadraIdAndDiaSemana(quadra.getId(), horarioFuncionamentoAtualizacao.diaSemana());

        if (existeReservasNoDiaSemana.isEmpty()){
            return;
        }

        var listaReservas = existeReservasNoDiaSemana
                .stream().filter(r -> r.getInicio().isBefore(abertura) || r.getEncerramento().isAfter(fechamento)).toList();

        if (!listaReservas.isEmpty()){
            throw new IllegalArgumentException("Ação não pode ser concluída porque a mudança atinge reservas já feitas.");
        }
    }
}
