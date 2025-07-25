package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, String> {
    List<Reserva> findAllByQuadraIdAndDataIsBetween(Long quadraId, LocalDateTime dataAbertura, LocalDateTime dataEncerramento);
    List<Reserva> findAllByQuadraIdAndDiaSemana(Long quadraId, DiaSemana diaSemana);

    boolean existsByQuadraIdAndDiaSemana(Long quadraId, DiaSemana diaSemana);

    boolean existsByQuadraId(Long quadraId);
    boolean existsByUsuarioEmail(String email);
}
