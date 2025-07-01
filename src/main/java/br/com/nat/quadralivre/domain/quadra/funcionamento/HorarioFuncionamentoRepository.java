package br.com.nat.quadralivre.domain.quadra.funcionamento;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamento, Long> {
    boolean existsByDiaAndQuadraId(DiaSemana diaSemana, Long quadraId);
    List<HorarioFuncionamento> findAllByQuadraId(Long quadraId);
    Optional<HorarioFuncionamento> findByDiaAndQuadraId(DiaSemana diaSemana, Long quadraId);
}
