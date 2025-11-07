package br.com.nat.quadralivre.domain.usuario;

import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorDadosSaoUnicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Autowired
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    private Usuario buscarUsuario(Usuario usuario){
        return this.usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new NoSuchElementException("Usuário autenticado não encontrado ou sessão inválida."));
    }

    public UsuarioDadosDetalhados buscar(Usuario usuarioRequisicao) {
        var usuario = this.buscarUsuario(usuarioRequisicao);
        return new UsuarioDadosDetalhados(usuario);
    }

    public UsuarioDadosDetalhados registrar(UsuarioRegistro usuarioRegistro){
        this.validadorDadosSaoUnicos.validar(usuarioRegistro);

        Usuario usuario = new Usuario(usuarioRegistro);
        usuario.setSenha(this.passwordEncoder.encode(usuarioRegistro.senha()));

        return new UsuarioDadosDetalhados(this.usuarioRepository.save(usuario));
    }

    public UsuarioDadosDetalhados atualizar(UsuarioAtualizacao usuarioAtualizacao, Usuario usuarioRequisicao){
        var usuario = this.buscarUsuario(usuarioRequisicao);
        usuario.atualizar(usuarioAtualizacao);
        return new UsuarioDadosDetalhados(usuario);
    }

    public void deletar(Usuario usuarioRequisicao){
        var usuario = this.buscarUsuario(usuarioRequisicao);
        this.validadorAcaoAtingeOutraEntidade.validar(usuario);
        this.usuarioRepository.delete(usuario);
    }
}
