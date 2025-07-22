package br.com.nat.quadralivre.domain.reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, String> {
    List<Reserva> findAllByQuadraIdAndDataIsBetween(Long quadraId, LocalDateTime dataAbertura, LocalDateTime dataEncerramento);

    boolean existsByQuadraId(Long quadraId);

    boolean existsByUsuarioEmail(String email);
}
