package br.com.nat.quadralivre.domain.quadra.funcionamento;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes.ValidadorDadosSaoUnicos;
import br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes.ValidadorHorarioAberturaFechamento;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class HorarioFuncionamentoService {

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Autowired
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    @Autowired
    private ValidadorHorarioAberturaFechamento validadorHorarioAberturaFechamento;

    private HorarioFuncionamento buscarPorHorarioFuncionamento(DiaSemana diaSemana, Long quadraId){
        return this.horarioFuncionamentoRepository.findByDiaAndQuadraId(diaSemana, quadraId)
                .orElseThrow(() -> new NoSuchElementException("Não existe horário de funcionamento para o dia indicado."));
    }

    public HorarioFuncionamentoDadosDetalhados registrar(Long quadraId, HorarioFuncionamentoRegistro registro, Usuario gestor){
        var quadra = this.quadraService.buscarQuadraParaGestor(quadraId, gestor);

        this.validadorDadosSaoUnicos.validar(registro.diaSemana(), quadra.getId());
        this.validadorHorarioAberturaFechamento.validar(registro.abertura(), registro.fechamento());

        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(registro);
        horarioFuncionamento.setQuadra(quadra);

        return new HorarioFuncionamentoDadosDetalhados(this.horarioFuncionamentoRepository.save(horarioFuncionamento));
    }

    public HorarioFuncionamentoLista buscarHorarioFuncionamento(Long quadraId){
        var funcionamento = this.horarioFuncionamentoRepository.findAllByQuadraId(quadraId);
        return new HorarioFuncionamentoLista(funcionamento.stream().map(HorarioFuncionamentoDadosDetalhados::new).toList());
    }

    public HorarioFuncionamentoDadosDetalhados atualizar(Long quadraId, HorarioFuncionamentoAtualizacao atualizacao, Usuario gestor){
        var quadra = this.quadraService.buscarQuadraParaGestor(quadraId, gestor);
        var horarioFuncionamento = this.buscarPorHorarioFuncionamento(atualizacao.diaSemana(), quadra.getId());

        this.validadorHorarioAberturaFechamento.validar(atualizacao.abertura(), atualizacao.fechamento());
        this.validadorAcaoAtingeOutraEntidade.validar(atualizacao, horarioFuncionamento);

        horarioFuncionamento.atualizar(atualizacao);
        return new HorarioFuncionamentoDadosDetalhados(horarioFuncionamento);
    }

    public void deletarFuncionamento(Long quadraId, DiaSemana diaSemana, Usuario gestor){
        var quadra = this.quadraService.buscarQuadraParaGestor(quadraId, gestor);
        var horarioFuncionamento = this.buscarPorHorarioFuncionamento(diaSemana, quadra.getId());

        this.validadorAcaoAtingeOutraEntidade.validar(quadra.getId(), diaSemana);
        this.horarioFuncionamentoRepository.deleteById(horarioFuncionamento.getId());
    }
}
