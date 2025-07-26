package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.usuario.autenticacao.UsuarioAutenticacao;
import br.com.nat.quadralivre.domain.usuario.autenticacao.UsuarioAutenticacaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacao")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Autenticação Controller", description = "Gerencia a autenticação do usuário no sistema.")
public class AutenticacaoController {

    @Autowired
    private UsuarioAutenticacaoLoginService usuarioService;

    @PostMapping
    @Operation(summary = "Efetuar login do usuário", description = "Faz login do usuário no sistema e retorna um token JWT.")
    public ResponseEntity efetuarLogin(@RequestBody @Valid UsuarioAutenticacao usuarioLogin){
        var dados = this.usuarioService.login(usuarioLogin);
        return ResponseEntity.ok(dados);
    }
}
