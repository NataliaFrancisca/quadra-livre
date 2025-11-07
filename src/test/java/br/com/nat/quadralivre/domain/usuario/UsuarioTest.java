package br.com.nat.quadralivre.domain.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setupUsuario(){
        var usuario = new Usuario();
        usuario.setNome("Maira Fernanda");
        usuario.setSenha("214365");
        usuario.setTelefone("11 20203030");
        this.usuario = usuario;
    }

    @Test
    @DisplayName("Deve atualizar apenas o nome quando os demais campos são nulos")
    void deveAtualizarSomenteNome() {
        var usuarioAtualizado = new UsuarioAtualizacao(
                null, "Maira Da Silva", null
        );

        usuario.atualizar(usuarioAtualizado);

        assertEquals(usuario.getNome(), usuarioAtualizado.nome());
        assertEquals(usuario.getSenha(), "214365");
        assertEquals(usuario.getTelefone(), "11 20203030");
    }

    @Test
    @DisplayName("Deve atualizar todos os campos quando não nulos")
    void deveAtualizarTodosOsCampos(){
        var usuarioAtualizado = new UsuarioAtualizacao(
                "987654", "Maira Calderano", "11 90903030"
        );

        usuario.atualizar(usuarioAtualizado);

        assertEquals(usuarioAtualizado.nome(), usuario.getNome());
        assertEquals(usuarioAtualizado.senha(), usuario.getSenha());
        assertEquals(usuarioAtualizado.telefone(), usuario.getTelefone());
    }

    @Test
    @DisplayName("Não deve realizar nenhuma atualização quando todos os campos são nulos")
    void naoDeveAtualizarNenhumCampo(){
        var usuarioAtualizado = new UsuarioAtualizacao(
                null, null, null
        );

        usuario.atualizar(usuarioAtualizado);

        assertEquals(usuario.getNome(), "Maira Fernanda");
        assertEquals(usuario.getSenha(), "214365");
        assertEquals(usuario.getTelefone(), "11 20203030");
    }
}