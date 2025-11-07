package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuário Controller", description = "Gerencia os usuários do sistema.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Registrar usuário", description = "Faz registro do usuário no sistema.")
    @Transactional
    public ResponseEntity registrarUsuario(@RequestBody @Valid UsuarioRegistro usuarioRegistro, UriComponentsBuilder uriBuilder){
        var usuario = this.usuarioService.registrar(usuarioRegistro);

        var uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(usuario.id())
                .toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping
    @Operation(summary = "Busca usuário", description = "Busca um usuário no sistema.")
    public ResponseEntity buscarUsuario(Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.usuarioService.buscar(usuario));
    }

    @PutMapping
    @Operation(summary = "Atualizar usuário", description = "Atualiza o usuário no sistema.")
    @Transactional
    public ResponseEntity atualizarUsuario(@RequestBody @Valid UsuarioAtualizacao usuarioAtualizacao, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.usuarioService.atualizar(usuarioAtualizacao, usuario));
    }

    @DeleteMapping
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário no sistema.")
    @Transactional
    public ResponseEntity deletarUsuario(Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        this.usuarioService.deletar(usuario);
        return ResponseEntity.noContent().build();
    }
}
