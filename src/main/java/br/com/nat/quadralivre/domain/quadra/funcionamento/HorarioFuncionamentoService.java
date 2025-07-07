package br.com.nat.quadralivre.domain.quadra.funcionamento;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.Indisponibilidade;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeDadosDetalhados;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeRegistro;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeRepository;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class HorarioFuncionamentoService {

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private IndisponibilidadeRepository indisponibilidadeRepository;

    private void verificarSeHorarioFuncionamentoJaFoiRegistrado(DiaSemana diaSemana, Long quadraId){
        var funcionamentoJaExisteParaDiaDaSemana = this.horarioFuncionamentoRepository.existsByDiaAndQuadraId(diaSemana, quadraId);

        if (funcionamentoJaExisteParaDiaDaSemana) {
            throw new DataIntegrityViolationException("Já existe um cadastro para esse dia da semana.");
        }
    }

    private HorarioFuncionamento verificarExisteHorarioFuncionamentoParaDiaIndicado(DiaSemana diaSemana, Long quadraId){
        var horarioFuncionamento = this.horarioFuncionamentoRepository.findByDiaAndQuadraId(diaSemana, quadraId);

        if (horarioFuncionamento.isEmpty()){
            throw new NoSuchElementException("Não existe horário de funcionamento para o dia indicado.");
        }

        return horarioFuncionamento.get();
    }

    private HorarioFuncionamento buscarHorarioFuncionamentoComDadosJaValidados(HorarioFuncionamentoParametros parametros){
        var quadra = this.quadraService.verificarQuadraExiste(parametros.quadraId());
        this.quadraService.verificarSeGestorResponsavelPelaQuadra(quadra.getGestor(), parametros.gestor());

        return this.verificarExisteHorarioFuncionamentoParaDiaIndicado(parametros.diaSemana(), parametros.quadraId());
    }

    public HorarioFuncionamentoDadosDetalhados registarFuncionamento(HorarioFuncionamentoParametros horarioFuncionamentoParametros, HorarioFuncionamentoRegistro registro){
        var quadra = this.quadraService.verificarQuadraExiste(horarioFuncionamentoParametros.quadraId());
        this.verificarSeHorarioFuncionamentoJaFoiRegistrado(registro.diaSemana(), horarioFuncionamentoParametros.quadraId());
        this.quadraService.verificarSeGestorResponsavelPelaQuadra(quadra.getGestor(), horarioFuncionamentoParametros.gestor());

        HorarioFuncionamento horarioFuncionamentoQuadra = new HorarioFuncionamento(registro);
        horarioFuncionamentoQuadra.setQuadra(quadra);
        var quadraSalva = this.horarioFuncionamentoRepository.save(horarioFuncionamentoQuadra);

        return new HorarioFuncionamentoDadosDetalhados(quadraSalva);
    }

    public HorarioFuncionamentoLista buscarHorarioFuncionamento(Long quadraId){
        var horarioFuncionamento = this.horarioFuncionamentoRepository.findAllByQuadraId(quadraId);
        var horarioFuncionamentoLista = horarioFuncionamento.stream().map(HorarioFuncionamentoDadosDetalhados::new).toList();

        return new HorarioFuncionamentoLista(horarioFuncionamentoLista);
    }

    public HorarioFuncionamentoDadosDetalhados atualizarFuncionamento(HorarioFuncionamentoParametros horarioFuncionamentoParametros, HorarioFuncionamentoAtualizacao atualizacao){
        var horarioFuncionamentoQuadra = this.buscarHorarioFuncionamentoComDadosJaValidados(horarioFuncionamentoParametros);
        horarioFuncionamentoQuadra.atualizar(atualizacao);

        this.horarioFuncionamentoRepository.save(horarioFuncionamentoQuadra);
        return new HorarioFuncionamentoDadosDetalhados(horarioFuncionamentoQuadra);
    }

    public void deletarFuncionamento(HorarioFuncionamentoParametros horarioFuncionamentoParametros){
        var horarioFuncionamentoQuadra = this.buscarHorarioFuncionamentoComDadosJaValidados(horarioFuncionamentoParametros);
        this.horarioFuncionamentoRepository.deleteById(horarioFuncionamentoQuadra.getId());
    }

    public IndisponibilidadeDadosDetalhados setarQuadraComoIndisponivelEmDataSelecionada(IndisponibilidadeRegistro indisponibilidadeRegistro, Usuario usuario){
        var quadra = this.quadraService.verificarQuadraExiste(indisponibilidadeRegistro.quadraId());
        this.quadraService.verificarSeGestorResponsavelPelaQuadra(usuario, quadra.getGestor());

        Indisponibilidade indisponibilidade = new Indisponibilidade();
        indisponibilidade.setData(indisponibilidadeRegistro.data());
        indisponibilidade.setQuadra(quadra);

        var dados = this.indisponibilidadeRepository.save(indisponibilidade);
        return new IndisponibilidadeDadosDetalhados(dados);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void atualizarDisponibilidadeDasQuadras(){
        LocalDate dataAtual = LocalDate.now();
        DiaSemana diaSemana = DiaSemana.fromEnglish(dataAtual.getDayOfWeek().toString());

        var datasPassadas = this.indisponibilidadeRepository.findAllByDataBefore(dataAtual);
        System.out.println(datasPassadas);

        for (var indisponibilidade: datasPassadas){
            var quadra = indisponibilidade.getQuadra();

            var diaHorarioFuncionamento = this.horarioFuncionamentoRepository
                    .findByDiaAndQuadraId(diaSemana, quadra.getId());

            if (diaHorarioFuncionamento.isEmpty()){
                return;
            }

            indisponibilidadeRepository.delete(indisponibilidade);
        }
    }
}
