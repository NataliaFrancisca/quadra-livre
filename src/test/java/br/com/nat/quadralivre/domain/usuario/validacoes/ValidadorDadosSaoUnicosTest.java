package br.com.nat.quadralivre.domain.usuario.validacoes;

import br.com.nat.quadralivre.domain.usuario.Role;
import br.com.nat.quadralivre.domain.usuario.UsuarioRegistro;
import br.com.nat.quadralivre.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidadorDadosSaoUnicosTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private ValidadorDadosSaoUnicos validador;

    private UsuarioRegistro usuario;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUpUsuario(){
        this.usuario = new UsuarioRegistro(
                "natalia@mail.com",
                "12345",
                "Natalia",
                "11 20203030",
                "12345678910",
                Role.USUARIO);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o E-mail já existe.")
    void deveLancarExcecaoQuandoEmailJaExiste(){
        when(repository.existsByEmail("natalia@mail.com")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> validador.validar(usuario));

        verify(repository).existsByEmail("natalia@mail.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CPF já existe.")
    void deveLancarExcecaoQuandoCPFJaExiste(){
        when(repository.existsByCpf("12345678910")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> validador.validar(usuario));

        verify(repository).existsByCpf("12345678910");
    }

    @Test
    @DisplayName("Deve passar quando não existe dados duplicados.")
    void devePassarQuandoNaoExisteDuplicidade(){
        var usuario = new UsuarioRegistro(
                "joao@mail.com",
                "12345",
                "Joao",
                "11 30302020",
                "98765432100",
                Role.USUARIO);

        when(repository.existsByEmail("joao@mail.com")).thenReturn(false);
        when(repository.existsByCpf("98765432100")).thenReturn(false);

        validador.validar(usuario);

        verify(repository).existsByEmail("joao@mail.com");
        verify(repository).existsByCpf("98765432100");
    }

}