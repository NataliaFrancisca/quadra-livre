package br.com.nat.quadralivre.domain.usuario;

import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorDadosSaoUnicos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Mock
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    private Usuario usuario;

    @BeforeEach
    void setupUsuario(){
        Usuario usuarioRequisicao = new Usuario();
        usuarioRequisicao.setId(1L);
        usuarioRequisicao.setNome("Natalia");
        usuarioRequisicao.setEmail("natalia@mail.com");
        usuarioRequisicao.setSenha("120409");
        usuarioRequisicao.setTelefone("11 20203030");
        this.usuario = usuarioRequisicao;
    }

    @Test
    @DisplayName("Deve registrar um usuário com sucesso")
    void deveRegistrarUmUsuarioComSucesso(){
        var registro = new UsuarioRegistro(
                "natalia@mail.com",
                "12345",
                "Natalia",
                "11 20203030",
                "1234568910",
                Role.USUARIO
        );

        when(passwordEncoder.encode("12345")).thenReturn("senhaCriptografada");
        when(repository.save(any(Usuario.class))).thenReturn(this.usuario);

        var resultado = usuarioService.registrar(registro);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Natalia", resultado.nome());

        verify(validadorDadosSaoUnicos).validar(registro);
        verify(passwordEncoder).encode("12345");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar um usuário com dados válidos")
    void deveRetornarUmUsuarioComDadosValidos(){
        when(repository.findById(this.usuario.getId())).thenReturn(Optional.of(this.usuario));
        var resultado = usuarioService.buscar(this.usuario);
        assertEquals(resultado.nome(), this.usuario.getNome());
        assertEquals(resultado.id(), this.usuario.getId());
    }

    @Test
    @DisplayName("Deve lançar uma exceção se o usuário não existir")
    void deveLancarUmaExcecaoSeUsuarioNaoExistir(){
        when(repository.findById(this.usuario.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> usuarioService.buscar(this.usuario));
    }

    @Test
    @DisplayName("Deve atualizar os dados do usuário com sucesso")
    void deveAtualizarDadosDoUsuario(){
        var usuarioAtualizacao = new UsuarioAtualizacao(
                "120409",
                "Natalia Francisca",
                null
        );

        when(repository.findById(this.usuario.getId())).thenReturn(Optional.of(this.usuario));
        var resultado = this.usuarioService.atualizar(usuarioAtualizacao, this.usuario);
        assertEquals("Natalia Francisca", resultado.nome());
        assertEquals("120409", resultado.senha());
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deveDeletarUmUsuario(){
        when(repository.findById(this.usuario.getId())).thenReturn(Optional.of(this.usuario));
        this.usuarioService.deletar(this.usuario);
        verify(validadorAcaoAtingeOutraEntidade).validar(this.usuario);
        verify(repository).delete(any(Usuario.class));
    }
}