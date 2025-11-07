package br.com.nat.quadralivre.domain.usuario.autenticacao;

import br.com.nat.quadralivre.domain.usuario.Usuario;
import br.com.nat.quadralivre.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticacaoLoginServiceTest {

    @Mock
    private AuthenticationManager manager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UsuarioAutenticacaoLoginService service;

    @Test
    @DisplayName("Deve retornar token quando autenticação é válida")
    void deveRetornarTokenQuandoAutenticaoForValida(){
        var usuario = new UsuarioAutenticacao("natalia@mail.com", "123456");
        var user = new Usuario();
        user.setEmail("natalia@mail.com");

        var authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        when(manager.authenticate(any())).thenReturn(authentication);
        when(tokenService.gerarToken(user)).thenReturn("token-jwt-123");

        var resultado = service.login(usuario);

        assertEquals("token-jwt-123", resultado.token());
        verify(manager).authenticate(any());
        verify(tokenService).gerarToken(user);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autenticação falhar")
    void deveLancarExcecaoQuandoAutenticacaoFalhar(){
        var usuario = new UsuarioAutenticacao("natalia@mail.com", "123456");

        when(manager.authenticate(any()))
                .thenThrow(new RuntimeException("Algo deu errado ao tentar realizar login. Verifique seus dados."));

        assertThrows(RuntimeException.class, () -> service.login(usuario));
    }
}