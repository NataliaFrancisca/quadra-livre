package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuadraRepository extends JpaRepository<Quadra, Long> {
    boolean existsByNomeAndEndereco(String nome, Endereco endereco);
    boolean existsByGestorEmail(String email);
}
