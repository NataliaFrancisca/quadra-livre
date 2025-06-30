package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.quadra.QuadraAtualizacao;
import br.com.nat.quadralivre.domain.quadra.QuadraRegistro;
import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.*;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/quadras")
public class QuadraController {

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private HorarioFuncionamentoService horarioFuncionamentoSerivce;

    private URI generateURI(Long id, UriComponentsBuilder uriBuilder){
        return uriBuilder
                .path("/quadras/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    @PostMapping
    public ResponseEntity registrarQuadra(@RequestBody @Valid QuadraRegistro quadraRegistro, UriComponentsBuilder uri, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        var quadra = this.quadraService.registrar(quadraRegistro, gestor);
        return ResponseEntity.created(this.generateURI(quadra.id(), uri)).body(quadra);
    }

    @GetMapping
    public ResponseEntity buscarQuadras(){
        var quadras = this.quadraService.buscarQuadras();
        return ResponseEntity.ok(quadras);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarQuadra(@PathVariable Long id){
        var quadra = this.quadraService.buscarQuadra(id);
        return ResponseEntity.ok(quadra);
    }

    @PutMapping
    public ResponseEntity atualizarQuadra(@RequestBody @Valid QuadraAtualizacao quadraAtualizacao, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        var quadraAtualizada = this.quadraService.atualizarQuadra(quadraAtualizacao, gestor);
        return ResponseEntity.ok(quadraAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarQuadra(@PathVariable Long id, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        this.quadraService.deletarQuadra(id, gestor);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/horario-funcionamento/{id}")
    public ResponseEntity definirHorarioFuncionamento(@PathVariable Long id, @RequestBody @Valid HorarioFuncionamentoRegistro horarioFuncionamentoRegistro, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        var paramentros = new HorarioFuncionamentoParametros(id, gestor, horarioFuncionamentoRegistro.diaSemana());

        var funcionamento = this.horarioFuncionamentoSerivce.registarFuncionamento(paramentros, horarioFuncionamentoRegistro);
        return ResponseEntity.ok(funcionamento);
    }

    @GetMapping("/horario-funcionamento/{id}")
    public ResponseEntity buscarHorarioFuncionamento(@PathVariable Long id){
        var funcionamento = this.horarioFuncionamentoSerivce.buscarHorarioFuncionamento(id);
        return ResponseEntity.ok(funcionamento);
    }

    @PutMapping("/horario-funcionamento/{id}")
    public ResponseEntity atualizarHorarioFuncionamento(@PathVariable Long id, @RequestBody @Valid HorarioFuncionamentoAtualizacao horarioFuncionamentoAtualizacao, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        var paramentros = new HorarioFuncionamentoParametros(id, gestor, horarioFuncionamentoAtualizacao.diaSemana());

        var funcionamento = this.horarioFuncionamentoSerivce.atualizarFuncionamento(paramentros, horarioFuncionamentoAtualizacao);
        return ResponseEntity.ok(funcionamento);
    }

    @DeleteMapping("/horario-funcionamento/{id}")
    public ResponseEntity deletarHorarioFuncionamento(@PathVariable Long id, @RequestParam DiaSemana diaSemana, Authentication authentication){
        var gestor = (Usuario) authentication.getPrincipal();
        var paramentros = new HorarioFuncionamentoParametros(id, gestor, diaSemana);

        this.horarioFuncionamentoSerivce.deletarFuncionamento(paramentros);
        return ResponseEntity.noContent().build();
    }
}
