package br.com.nat.quadralivre.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);

    @Query("SELECT u FROM Usuario u WHERE u.login LIKE :login")
    Optional<Usuario> buscarUsuarioPorCampoLogin(String login);

    boolean existsByLogin(String login);
    boolean existsByCpf(String cpf);
}
