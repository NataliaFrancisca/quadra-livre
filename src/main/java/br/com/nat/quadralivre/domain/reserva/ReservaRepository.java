package br.com.nat.quadralivre.domain.reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, String> {
    boolean existsByDataAndQuadraId(LocalDateTime dataSolicitada, Long quadraId);

    List<Reserva> findAllByQuadraIdAndDataIsBetween(Long quadraId, LocalDateTime dataAbertura, LocalDateTime dataEncerramento);
}
