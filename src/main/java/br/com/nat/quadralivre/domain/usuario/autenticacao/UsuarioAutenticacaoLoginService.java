package br.com.nat.quadralivre.domain.usuario.autenticacao;

import br.com.nat.quadralivre.domain.usuario.Usuario;
import br.com.nat.quadralivre.infra.security.DadosTokenJWT;
import br.com.nat.quadralivre.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticacaoLoginService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

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
}
