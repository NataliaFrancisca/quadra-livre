package br.com.nat.quadralivre.domain.usuario.autenticacao;

import br.com.nat.quadralivre.domain.usuario.Usuario;
import br.com.nat.quadralivre.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticacaoServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioAutenticacaoService autenticacaoService;

    @Test
    @DisplayName("Deve retornar um UserDetails quando o e-mail existir.")
    void loadUserByUsername() {
        var usuario = new Usuario();
        usuario.setEmail("natalia@mail.com");

        when(this.repository.findByEmail("natalia@mail.com"))
                .thenReturn(usuario);

        var resultado = this.autenticacaoService.loadUserByUsername("natalia@mail.com");
        assertEquals(usuario, resultado);
        verify(repository).findByEmail("natalia@mail.com");
    }
}