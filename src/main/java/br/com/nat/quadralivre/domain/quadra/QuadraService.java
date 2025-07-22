package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuadraService {

    @Autowired
    private QuadraRepository quadraRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HorarioFuncionamentoRepository quadraFuncionamentoRepository;

    private void verificarSeQuadraJaRegistradaNoEndereco(QuadraRegistro quadra){
        boolean quadraEstaRegistrada = this.quadraRepository.existsByNomeAndEndereco(quadra.nome(), quadra.endereco());

        if (quadraEstaRegistrada){
            throw new DataIntegrityViolationException("Quadra já cadastrada no sistema.");
        }
    }

    private void verificarSeAcoesAtigemOutrasEntidades(Long quadraId){
        var existeReservasParaQuadra = this.reservaRepository.existsByQuadraId(quadraId);

        if (existeReservasParaQuadra){
            throw new IllegalArgumentException("Ação não pode ser concluída, pois existe reservas para essa quadra.");
        }
    }

    public Quadra verificarQuadraExiste(Long id){
        Optional<Quadra> quadra = this.quadraRepository.findById(id);

        if (quadra.isEmpty()){
            throw new NoSuchElementException("Não existe registro de quadra com esse ID.");
        }

        return quadra.get();
    }

    public void verificarSeGestorResponsavelPelaQuadra(Usuario gestorQuadra, Usuario gestorAutenticacao){
        if (!gestorQuadra.getEmail().equals(gestorAutenticacao.getEmail())){
            throw new AccessDeniedException("Os dados da quadra só podem ser atualizados pelo responsável do local.");
        }
    }

    public QuadraDadosDetalhados registrar(QuadraRegistro quadraRegistro, Usuario usuario){
        this.verificarSeQuadraJaRegistradaNoEndereco(quadraRegistro);

        Quadra quadra = new Quadra(quadraRegistro);
        quadra.setGestor(usuario);

        var quadraSalva = this.quadraRepository.save(quadra);
        return new QuadraDadosDetalhados(quadraSalva);
    }

    public List<QuadraDadosDetalhados> buscarQuadras(){
        List<Quadra> quadras = this.quadraRepository.findAll();
        return quadras.stream().map(QuadraDadosDetalhados::new).toList();
    }

    public QuadraDadosDetalhados buscarQuadra(Long id){
        var quadra = this.verificarQuadraExiste(id);
        return new QuadraDadosDetalhados(quadra);
    }

    public QuadraDadosDetalhados atualizarQuadra(QuadraAtualizacao quadraAtualizacao, Usuario gestor){
        var quadra = this.verificarQuadraExiste(quadraAtualizacao.id());

        this.verificarSeAcoesAtigemOutrasEntidades(quadra.getId());
        this.verificarSeGestorResponsavelPelaQuadra(gestor, quadra.getGestor());

        quadra.atualizar(quadraAtualizacao);

        this.quadraRepository.save(quadra);

        return new QuadraDadosDetalhados(quadra);
    }

    public void deletarQuadra(Long id, Usuario gestor){
        var quadra = this.verificarQuadraExiste(id);

        this.verificarSeAcoesAtigemOutrasEntidades(quadra.getId());

        this.verificarSeGestorResponsavelPelaQuadra(gestor, quadra.getGestor());
        this.quadraRepository.deleteById(id);
    }
}
