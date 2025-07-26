package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import br.com.nat.quadralivre.domain.reserva.geracao.GeradorDeReservas;
import br.com.nat.quadralivre.domain.reserva.validacoes.ValidadorDadosSaoUnicos;
import br.com.nat.quadralivre.domain.reserva.validacoes.ValidadorQuadraEstaDisponivel;
import br.com.nat.quadralivre.domain.reserva.validacoes.ValidadorReservaDeveSerParaMesAtual;
import br.com.nat.quadralivre.domain.reserva.validacoes.ValidadorUsuarioPodeRealizarAcao;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservaService {
    @Autowired
    private GeradorDeReservas geradorDeReservas;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ValidadorUsuarioPodeRealizarAcao validadorUsuarioPodeRealizarAcao;

    @Autowired
    private ValidadorReservaDeveSerParaMesAtual validadorReservaDeveSerParaMesAtual;

    @Autowired
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Autowired
    private ValidadorQuadraEstaDisponivel validadorQuadraEstaDisponivel;

    private Reserva verificarReservaExiste(String id){
        return this.reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não existe reserva com esse número de ID."));
    }

    private ReservaDisponivel identificarReserva(ReservaRegistro reserva){
        ReservaPesquisa reservaBusca = new ReservaPesquisa(reserva.quadraId(), reserva.data());

        List<ReservaDisponivel> reservas = this.exibirReservasDisponiveis(reservaBusca);

        return reservas.stream()
                .filter(h -> h.getId().equals(reserva.reservaId()))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Não existe reserva com esse número de identificação."));
    }

    public List<ReservaDisponivel> exibirReservasDisponiveis(ReservaPesquisa reserva){
        this.quadraService.buscarQuadra(reserva.quadraId());

        DiaSemana diaSemana = DiaSemana.fromEnglish(reserva.data().getDayOfWeek().name());

        HorarioFuncionamento horarioFuncionamento = this.horarioFuncionamentoRepository
                .findByDiaAndQuadraId(diaSemana, reserva.quadraId()).orElseThrow(
                        () -> new NoSuchElementException("A quadra escolhida não têm horário de funcionamento cadastrado para o dia indicado."));

        this.validadorReservaDeveSerParaMesAtual.validar(reserva.data());
        this.validadorQuadraEstaDisponivel.validar(reserva, horarioFuncionamento);
        return this.geradorDeReservas.gerar(horarioFuncionamento, reserva.data());
    }

    public ReservaDadosAberto registrarReserva(ReservaRegistro reservaRegistro, Usuario usuario){
        var quadra = this.quadraService.buscarQuadra(reservaRegistro.quadraId());

        this.validadorDadosSaoUnicos.validar(reservaRegistro.reservaId());
        this.validadorReservaDeveSerParaMesAtual.validar(reservaRegistro.data());

        var reservaDisponivel = this.identificarReserva(reservaRegistro);

        Reserva reserva = new Reserva(reservaDisponivel);
        reserva.setQuadra(quadra);
        reserva.setUsuario(usuario);

        return new ReservaDadosAberto(this.reservaRepository.save(reserva));
    }

    public ReservaDadosDetalhados buscarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExiste(id);
        this.validadorUsuarioPodeRealizarAcao.validar(reserva, usuario);
        return new ReservaDadosDetalhados(reserva);
    }

    public void deletarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExiste(id);
        this.validadorUsuarioPodeRealizarAcao.validar(reserva, usuario);
        this.reservaRepository.deleteById(reserva.getId());
    }
}
