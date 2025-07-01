package br.com.nat.quadralivre.domain.usuario;

import br.com.nat.quadralivre.domain.reserva.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void verificarSeAcoesAtigemOutrasEntidades(String usuario){
        var usuarioTemReservas = this.reservaRepository.existsByUsuarioLogin(usuario);

        if (usuarioTemReservas){
            throw new IllegalArgumentException("Ação não pode ser concluida. Existe reservas para essa quadra.");
        }
    }

    public UsuarioDadosDetalhados registrar(UsuarioRegistro usuarioRegistro){
        Usuario usuario = new Usuario(usuarioRegistro);
        usuario.setSenha(this.passwordEncoder.encode(usuarioRegistro.senha()));

        return new UsuarioDadosDetalhados(this.usuarioRepository.save(usuario));
    }

    public UsuarioDadosDetalhados atualizar(String usuarioLogin, UsuarioAtualizacao usuarioAtualizacao){
        Optional<Usuario> usuario = this.usuarioRepository.buscarUsuarioPorCampoLogin(usuarioLogin);

        if (usuario.isEmpty()){
            throw new NoSuchElementException("Não existe usuário com esse endereço de e-mail.");
        }

        usuario.get().atualizar(usuarioAtualizacao);
        return new UsuarioDadosDetalhados(usuario.get());
    }

    public UsuarioDadosDetalhados buscar(Long id) {
        Optional<Usuario> usuario = this.usuarioRepository.findById(id);

        if (usuario.isEmpty()){
            throw new NoSuchElementException("Não existe usuário com esse número de id.");
        }

        return new UsuarioDadosDetalhados(usuario.get());
    }

    public void deletar(Long id){
        Optional<Usuario> usuario = this.usuarioRepository.findById(id);

        if (usuario.isEmpty()){
            throw new NoSuchElementException("Não existe usuário com esse número de id.");
        }

        this.verificarSeAcoesAtigemOutrasEntidades(usuario.get().getLogin());
        this.usuarioRepository.deleteById(id);
    }
}
