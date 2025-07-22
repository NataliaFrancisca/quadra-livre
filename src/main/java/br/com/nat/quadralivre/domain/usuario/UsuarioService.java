package br.com.nat.quadralivre.domain.usuario;

import br.com.nat.quadralivre.domain.usuario.autenticacao.UsuarioAutenticacao;
import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorDadosSaoUnicos;
import br.com.nat.quadralivre.domain.usuario.validacoes.ValidadorUsuarioPodeRealizarAcao;
import br.com.nat.quadralivre.infra.security.DadosTokenJWT;
import br.com.nat.quadralivre.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ValidadorUsuarioPodeRealizarAcao validadorUsuarioPodeRealizarAcao;

    @Autowired
    private ValidadorDadosSaoUnicos validadorDadosSaoUnicos;

    @Autowired
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    private Usuario buscarUsuario(Usuario usuario){
        return this.usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new NoSuchElementException("Usuário autenticado não encontrado ou sessão inválida."));
    }

    public DadosTokenJWT login(UsuarioAutenticacao usuario){
        try{
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuario.email(),
                    usuario.senha()
            );
            var auth = this.manager.authenticate(authenticationToken);
            var tokenJWT = this.tokenService.gerarToken((Usuario) auth.getPrincipal());
            return new DadosTokenJWT(tokenJWT);
        }catch (Exception ex){
            throw new RuntimeException("Algo deu errado ao tentar realizar login. Verifique seus dados.");
        }
    }

    public UsuarioDadosDetalhados registrar(UsuarioRegistro usuarioRegistro){
        this.validadorDadosSaoUnicos.validar(usuarioRegistro);

        Usuario usuario = new Usuario(usuarioRegistro);
        usuario.setSenha(this.passwordEncoder.encode(usuarioRegistro.senha()));

        return new UsuarioDadosDetalhados(this.usuarioRepository.save(usuario));
    }

    public UsuarioDadosDetalhados buscar(Long id, Usuario usuarioRequisicao) {
        this.validadorUsuarioPodeRealizarAcao.validar(usuarioRequisicao.getId(), id);
        var usuario = this.buscarUsuario(usuarioRequisicao);
        return new UsuarioDadosDetalhados(usuario);
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
