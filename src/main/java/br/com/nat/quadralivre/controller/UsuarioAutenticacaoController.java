package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.usuario.Usuario;
import br.com.nat.quadralivre.domain.usuario.UsuarioAutenticacao;
import br.com.nat.quadralivre.infra.security.DadosTokenJWT;
import br.com.nat.quadralivre.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class UsuarioAutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid UsuarioAutenticacao dados){
        try{
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            var auth = this.manager.authenticate(authenticationToken);

            var tokenJWT = this.tokenService.gerarToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
