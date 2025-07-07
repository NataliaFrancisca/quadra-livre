package br.com.nat.quadralivre.domain.quadra.indisponibilidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IndisponibilidadeRepository extends JpaRepository<Indisponibilidade, Long> {
    List<Indisponibilidade> findAllByData(LocalDate data);

    List<Indisponibilidade> findAllByDataBefore(LocalDate data);

    boolean existsByData(LocalDate dataSolicitada);
}
