package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private URI generateURI(Long id, UriComponentsBuilder uriBuilder){
        return uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    @PostMapping
    @Transactional
    public ResponseEntity registrarUsuario(@RequestBody @Valid UsuarioRegistro usuarioRegistro, UriComponentsBuilder uri){
        var usuario = this.usuarioService.registrar(usuarioRegistro);
        return ResponseEntity.created(this.generateURI(usuario.id(), uri)).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarUsuario(@PathVariable Long id, Authentication authentication){
        var usuarioLogado = (Usuario) authentication.getPrincipal();

        if (!usuarioLogado.getId().equals(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var usuario = this.usuarioService.buscar(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarUsuario(@RequestBody @Valid UsuarioAtualizacao usuarioAtualizacao, Authentication authentication){
        var usuarioLogado = (Usuario) authentication.getPrincipal();
        var usuario = this.usuarioService.atualizar(usuarioLogado.getLogin(), usuarioAtualizacao);

        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deletarUsuario(Authentication authentication){
        var usuarioLogado = (Usuario) authentication.getPrincipal();
        this.usuarioService.deletar(usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }
}
