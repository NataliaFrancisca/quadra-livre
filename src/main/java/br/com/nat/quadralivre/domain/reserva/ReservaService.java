package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamentoRepository;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeRepository;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Autowired
    private IndisponibilidadeRepository indisponibilidadeRepository;

    private void verificarSeReservaJaFoiRealizada(String idReserva){
        if (idReserva == null || idReserva.isBlank()) {
            throw new IllegalArgumentException("O ID da reserva não pode ser nulo ou vazio.");
        }

        boolean reservaJaExiste = this.reservaRepository.existsById(idReserva);

        if (reservaJaExiste){
            throw new DataIntegrityViolationException("Já existe uma reserva para a data e horário indicado.");
        }
    }

    private ReservaDisponivel buscarReservaParaUsuario(ReservaRegistro reservaDoUsuario){
        ReservaBusca reservaBusca = new ReservaBusca(reservaDoUsuario.quadraId(), reservaDoUsuario.data());

        List<ReservaDisponivel> reservasDisponiveis = this.exibirReservas(reservaBusca);

        return reservasDisponiveis.stream()
                .filter(h -> h.id().equals(reservaDoUsuario.reservaId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não existe reserva livre com esse número de identificação."));
    }

    private Reserva verificarReservaExiste(String id){
        return this.reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não existe reserva com esse número de ID."));
    }

    private void verificarUsuarioPodeRealizarAcao(Reserva reserva, Usuario usuario){
        var usuarioReserva = reserva.getUsuario().getEmail();
        var gestorDaQuadra = reserva.getQuadra().getGestor().getEmail();

        if (!usuarioReserva.equals(usuario.getEmail())){
            if (!gestorDaQuadra.equals(usuario.getEmail())){
                throw new AccessDeniedException("Somente o usuário ou gestor responsável pela quadra pode acessar esses dados.");
            }
        }
    }

    private void verificarSeReservaEParaOMesAtual(ReservaRegistro reservaRegistro){
        LocalDate dataReserva = LocalDate.of(reservaRegistro.data().getYear(), reservaRegistro.data().getMonth(), 1);
        LocalDate dataAtual = LocalDate.now();

        if (dataReserva.isEqual(dataAtual)){
            return;
        }

        boolean eUltimoDiaDoMes = dataAtual.getDayOfMonth() == dataAtual.lengthOfMonth();
        var primeiroDiaDoProximoMes = dataAtual.plusMonths(1).withDayOfMonth(1);

        boolean mesmaReferenciaDeMes = dataReserva.getMonth() == primeiroDiaDoProximoMes.getMonth()
                && dataReserva.getYear() == primeiroDiaDoProximoMes.getYear();

        if (eUltimoDiaDoMes && mesmaReferenciaDeMes){
            return;
        }

        throw new IllegalArgumentException("Reservas para meses futuros só são liberadas no último dia do mês anterior.");
    }

    private void verificarSeQuadraTemEventoDeIndisponibilidadeNaDataSelecionada(LocalDate dataSolicitada){
        var existeIndisponibilidade = this.indisponibilidadeRepository.existsByData(dataSolicitada);

        if (existeIndisponibilidade){
            throw new IllegalArgumentException("Quadra não vai estar disponível no dia indicado.");
        }
    }

    public List<ReservaDisponivel> exibirReservas(ReservaBusca reservaBusca){
        this.verificarSeQuadraTemEventoDeIndisponibilidadeNaDataSelecionada(reservaBusca.data());

        this.quadraService.verificarQuadraExiste(reservaBusca.quadraId());

        DayOfWeek data = reservaBusca.data().getDayOfWeek();
        DiaSemana diaSemana = DiaSemana.fromEnglish(data.toString());

        var horarioFuncionamento = this.horarioFuncionamentoRepository.findByDiaAndQuadraId(diaSemana, reservaBusca.quadraId());

        if (horarioFuncionamento.isEmpty()){
            throw new NoSuchElementException("Não existe quadra para esse dia da semana");
        }

        if (!horarioFuncionamento.get().isDisponibilidade()){
            throw new IllegalArgumentException("Quadra não disponível no dia indicado.");
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

        this.verificarSeReservaEParaOMesAtual(reservaRegistro);

        var reservaSalva = this.reservaRepository.save(reserva);
        return new ReservaDadosAberto(reservaSalva);
    }

    public ReservaDadosDetalhados buscarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExiste(id);
        this.verificarUsuarioPodeRealizarAcao(reserva, usuario);
        return new ReservaDadosDetalhados(reserva);
    }

    public void deletarReserva(String id, Usuario usuario) {
        var reserva = this.verificarReservaExiste(id);
        this.verificarUsuarioPodeRealizarAcao(reserva, usuario);
        this.reservaRepository.deleteById(reserva.getId());
    }
}
