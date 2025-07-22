package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.usuario.*;
import br.com.nat.quadralivre.domain.usuario.autenticacao.UsuarioAutenticacao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid UsuarioAutenticacao usuarioLogin){
        var dados = this.usuarioService.login(usuarioLogin);
        return ResponseEntity.ok(dados);
    }

    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity registrarUsuario(@RequestBody @Valid UsuarioRegistro usuarioRegistro, UriComponentsBuilder uriBuilder){
        var usuario = this.usuarioService.registrar(usuarioRegistro);

        var uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(usuario.id())
                .toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarUsuario(@PathVariable Long id, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.usuarioService.buscar(id, usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarUsuario(@RequestBody @Valid UsuarioAtualizacao usuarioAtualizacao, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.usuarioService.atualizar(usuarioAtualizacao, usuario));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deletarUsuario(Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        this.usuarioService.deletar(usuario);
        return ResponseEntity.noContent().build();
    }
}
