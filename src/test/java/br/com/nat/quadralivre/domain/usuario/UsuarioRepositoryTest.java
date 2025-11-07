package br.com.nat.quadralivre.domain.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.flyway.enabled=false")
class UsuarioRepositoryTest {
    @Autowired UsuarioRepository repository;

    @BeforeEach
    void criarUsuario(){
        repository.deleteAll();

        Usuario usuario = new Usuario();

        usuario.setNome("Natalia");
        usuario.setEmail("natalia@mail.com");
        usuario.setSenha("12345678");
        usuario.setTelefone("11 20203030");
        usuario.setCpf("12345678910");
        usuario.setRole(Role.USUARIO);

        repository.save(usuario);
    }

    @Test
    @DisplayName("Deve verificar se um usuário existe pelo seu e-mail")
    void deveVerificarUsuarioPorEmail(){
        boolean encontrado = repository.existsByEmail("natalia@mail.com");
        assertThat(encontrado).isTrue();
    }

    @Test
    @DisplayName("Deve verificar se um usuário existe pelo seu CPF")
    void deveVerificarUsuarioPorCPF(){
        boolean encontrado = repository.existsByCpf("12345678910");
        assertThat(encontrado).isTrue();
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo seu e-mail")
    void deveEncontrarUsuarioPeloEmail(){
        UserDetails encontrado = repository.findByEmail("natalia@mail.com");
        assertThat(encontrado.getUsername()).isEqualTo("natalia@mail.com");
    }

    @Test
    @DisplayName("Deve verificar quando um usuário não existe")
    void deveVerificarUsuarioNaoExiste(){
        boolean encontrado = repository.existsByEmail("joao@mail.com");
        assertThat(encontrado).isFalse();
    }
}