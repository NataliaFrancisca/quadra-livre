package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.quadra.validacoes.ValidadorDadosSaoUnicos;
import br.com.nat.quadralivre.domain.quadra.validacoes.ValidadorUsuarioPodeRealizarAcao;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class QuadraService {

    @Autowired
    private QuadraRepository quadraRepository;

    @Autowired
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Autowired
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    @Autowired
    private ValidadorUsuarioPodeRealizarAcao validadorUsuarioPodeRealizarAcao;

    public Quadra buscarQuadra(Long id){
        return this.quadraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não existe quadra com esse número de ID."));
    }

    public Quadra buscarQuadraParaGestor(Long quadraId, Usuario gestor){
        var quadra = this.buscarQuadra(quadraId);
        this.validadorUsuarioPodeRealizarAcao.validar(quadra, gestor);
        return quadra;
    }

    public QuadraDadosDetalhados registrar(QuadraRegistro quadraRegistro, Usuario gestor){
        this.validadorDadosSaoUnicos.validar(quadraRegistro);

        Quadra quadra = new Quadra(quadraRegistro);
        quadra.setGestor(gestor);

        var quadraSalva = this.quadraRepository.save(quadra);
        return new QuadraDadosDetalhados(quadraSalva);
    }

    public QuadraDadosDetalhados buscarDadosQuadra(Long id){
        var quadra = this.buscarQuadra(id);
        return new QuadraDadosDetalhados(quadra);
    }

    public List<QuadraDadosDetalhados> buscarQuadras(){
        List<Quadra> quadras = this.quadraRepository.findAll();
        return quadras.stream().map(QuadraDadosDetalhados::new).toList();
    }

    public QuadraDadosDetalhados atualizarQuadra(QuadraAtualizacao quadraAtualizacao, Usuario gestorRequisicao){
        var quadra = this.buscarQuadraParaGestor(quadraAtualizacao.id(), gestorRequisicao);

        this.validadorAcaoAtingeOutraEntidade.validar(quadra.getId());
        quadra.atualizar(quadraAtualizacao);
        return new QuadraDadosDetalhados(this.quadraRepository.save(quadra));
    }

    public void deletarQuadra(Long id, Usuario gestorRequisicao){
        var quadra = this.buscarQuadraParaGestor(id, gestorRequisicao);

        this.validadorAcaoAtingeOutraEntidade.validar(quadra.getId());
        this.quadraRepository.deleteById(id);
    }
}
