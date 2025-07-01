package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservaService {
    @Autowired
    private GerarReservas gerarReservas;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private ReservaRepository reservaRepository;

    private void verificarSeReservaJaFoiRealizada(String idReserva){
        boolean reservaJaExiste = this.reservaRepository.existsById(idReserva);

        if (reservaJaExiste){
            throw new DataIntegrityViolationException("Já existe uma reserva para a data e horário indicado.");
        }
    }

    private ReservaDisponivel buscarReservaParaUsuario(ReservaRegistro reservaDoUsuario){
        ReservaBusca reservaBusca = new ReservaBusca(reservaDoUsuario.quadraId(), reservaDoUsuario.data());

        List<ReservaDisponivel> reservasDisponiveis = this.exibirReservas(reservaBusca);

        Optional<ReservaDisponivel> reserva = reservasDisponiveis.stream()
                .filter(h -> h.id().equals(reservaDoUsuario.reservaId()))
                .findFirst();

        if (reserva.isEmpty()){
            throw new IllegalArgumentException("Não existe reserva livre com esse número de identificação.");
        }

        return reserva.get();
    }

    private Reserva verificarReservaExisteEUsuarioPodeRealizarAcao(String id, Usuario usuario){
        var reservaExiste = this.reservaRepository.findById(id);

        if (reservaExiste.isEmpty()){
            throw new NoSuchElementException("Não existe reserva com esse número de ID.");
        }

        var reserva = reservaExiste.get();

        var usuarioReserva = reserva.getUsuario().getLogin();
        var gestorDaQuadra = reserva.getQuadra().getGestor().getLogin();

        if (!usuarioReserva.equals(usuario.getLogin())){
            if (!gestorDaQuadra.equals(usuario.getLogin())){
                throw new IllegalArgumentException("Somente usuário ou gestor responsável pela quadra pode acessar esses dados.");
            }
        }

        return reserva;
    }

    public List<ReservaDisponivel> exibirReservas(ReservaBusca reservaBusca){
        this.quadraService.verificarQuadraExiste(reservaBusca.quadraId());

        DayOfWeek data = reservaBusca.data().getDayOfWeek();
        DiaSemana diaSemana = DiaSemana.fromEnglish(data.toString());

        var horarioFuncionamento = this.horarioFuncionamentoRepository.findByDiaAndQuadraId(diaSemana, reservaBusca.quadraId());

        if (horarioFuncionamento.isEmpty()){
            throw new NoSuchElementException("Não existe quadra para esse dia da semana");
        }

        return this.gerarReservas.gerar(horarioFuncionamento.get(), reservaBusca.data());
    }

    public ReservaDadosAberto registrarReserva(ReservaRegistro reservaRegistro, Usuario usuario){
        var quadra = this.quadraService.verificarQuadraExiste(reservaRegistro.quadraId());
        this.verificarSeReservaJaFoiRealizada(reservaRegistro.reservaId());
        var reservaDisponivel = this.buscarReservaParaUsuario(reservaRegistro);

        Reserva reserva = new Reserva(reservaDisponivel);
        reserva.setQuadra(quadra);
        reserva.setUsuario(usuario);

        var reservaSalva = this.reservaRepository.save(reserva);
        return new ReservaDadosAberto(reservaSalva);
    }

    public ReservaDadosDetalhados buscarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExisteEUsuarioPodeRealizarAcao(id, usuario);
        return new ReservaDadosDetalhados(reserva);
    }

    public void deletarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExisteEUsuarioPodeRealizarAcao(id, usuario);
        this.reservaRepository.deleteById(reserva.getId());
    }
}
