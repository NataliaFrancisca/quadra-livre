package br.com.nat.quadralivre.domain.usuario.validacoes;

import br.com.nat.quadralivre.domain.quadra.QuadraRepository;
import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import br.com.nat.quadralivre.domain.usuario.Role;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorAcaoAtingeOutraEntidadeTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private QuadraRepository quadraRepository;

    @InjectMocks
    private ValidadorAcaoAtingeOutraEntidade validador;

    private Usuario gestor;
    private Usuario usuario;

    @BeforeEach
    void setup(){
        var usuario = new Usuario();
        usuario.setRole(Role.USUARIO);
        usuario.setEmail("natalia@mail.com");
        usuario.setCpf( "12345678910");
        usuario.setTelefone("11 20203030");
        usuario.setNome("Natalia");
        usuario.setSenha("123456");
        this.usuario = usuario;

        var gestor = new Usuario();
        gestor.setRole(Role.GESTOR);
        gestor.setEmail("joao@mail.com");
        gestor.setCpf("98765432100");
        gestor.setTelefone("11 40402020");
        gestor.setNome("João");
        gestor.setSenha("1234567");
        this.gestor = gestor;
    }

    @Test
    @DisplayName("Lança uma exceção se o usuário tem uma reserva feita em seu e-mail")
    void lancaExcecaoSeUsuarioTemReservasNoSeuEmail(){
        when(reservaRepository.existsByUsuarioEmail("natalia@mail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> validador.validar(usuario));

        verify(reservaRepository).existsByUsuarioEmail("natalia@mail.com");
    }

    @Test
    @DisplayName("Lança uma exceção se o gestor tem uma quadra cadastrada em seu e-mail")
    void lancaExcecaoSeGestorTemQuadraCadastradaNoSeuEmail(){
        when(quadraRepository.existsByGestorEmail("joao@mail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> validador.validar(gestor));

        verify(quadraRepository).existsByGestorEmail("joao@mail.com");
    }
}