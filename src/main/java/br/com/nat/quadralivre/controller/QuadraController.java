package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.quadra.QuadraAtualizacao;
import br.com.nat.quadralivre.domain.quadra.QuadraRegistro;
import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.*;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeRegistro;
import br.com.nat.quadralivre.domain.quadra.indisponibilidade.IndisponibilidadeService;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/quadras")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Quadra Controller", description = "Gerencia as quadras do sistema.")
public class QuadraController {

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private HorarioFuncionamentoService horarioFuncionamentoSerivce;

    @Autowired
    private IndisponibilidadeService indisponibilidadeService;

    @PostMapping
    @Operation(summary = "Registrar quadra", description = "Cria uma nova quadra no sistema")
    public ResponseEntity registrarQuadra(@RequestBody @Valid QuadraRegistro quadraRegistro, UriComponentsBuilder uriBuilder, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        var quadra = this.quadraService.registrar(quadraRegistro, gestor);

        var uri = uriBuilder
                .path("/quadras/{id}")
                .buildAndExpand(quadra.id())
                .toUri();

        return ResponseEntity.created(uri).body(quadra);
    }

    @GetMapping
    @Operation(summary = "Buscar quadras", description = "Busca todas as quadras no sistema")
    public ResponseEntity buscarQuadras(){
        var quadras = this.quadraService.buscarQuadras();
        return ResponseEntity.ok(quadras);
    }

    @GetMapping("/{quadraId}")
    @Operation(summary = "Buscar quadra", description = "Busca uma quadra que atenda o id no sistema.")
    public ResponseEntity buscarQuadra(@PathVariable Long quadraId){
        var quadra = this.quadraService.buscarDadosQuadra(quadraId);
        return ResponseEntity.ok(quadra);
    }

    @PutMapping
    @Operation(summary = "Atualizar quadra", description = "Atualiza uma quadra no sistema")
    public ResponseEntity atualizarQuadra(@RequestBody @Valid QuadraAtualizacao quadraAtualizacao, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        var quadraAtualizada = this.quadraService.atualizarQuadra(quadraAtualizacao, gestor);
        return ResponseEntity.ok(quadraAtualizada);
    }

    @DeleteMapping("/{quadraId}")
    @Operation(summary = "Deletar quadra", description = "Deleta uma quadra que atendo o id no sistema")
    public ResponseEntity deletarQuadra(@PathVariable Long quadraId, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        this.quadraService.deletarQuadra(quadraId, gestor);
        return ResponseEntity.noContent().build();
    }

    // horario-funcionamento
    @PostMapping("/{quadraId}/horarios-funcionamento")
    @Operation(summary = "Registra um horário de funcionamento", description = "Registra um horário de funcionamento para quadra que atenda o id no sistema")
    public ResponseEntity definirHorarioFuncionamento(@PathVariable Long quadraId, @RequestBody @Valid HorarioFuncionamentoRegistro horarioFuncionamentoRegistro, Authentication auth, UriComponentsBuilder uriBuilder){
        var gestor = (Usuario) auth.getPrincipal();
        var horarioFuncionamento = this.horarioFuncionamentoSerivce
                .registrar(quadraId, horarioFuncionamentoRegistro, gestor);

        var uri = uriBuilder
                .path("/horario-funcionamento/{id}")
                .buildAndExpand(horarioFuncionamento.id())
                .toUri();

        return ResponseEntity.created(uri).body(horarioFuncionamento);
    }

    @GetMapping("/{quadraId}/horarios-funcionamento")
    @Operation(summary = "Busca horário de funcionamento", description = "Busca horário de funcionamento para quadra que atenda o id no sistema")
    public ResponseEntity buscarHorarioFuncionamento(@PathVariable Long quadraId){
        var horarioFuncionamento = this.horarioFuncionamentoSerivce.buscarHorarioFuncionamento(quadraId);
        return ResponseEntity.ok(horarioFuncionamento);
    }

    @PutMapping("/{quadraId}/horarios-funcionamento")
    @Operation(summary = "Atualiza um horário de funcionamento", description = "Atualiza um horário de funcionamento para quadra que atenda o id no sistema")
    public ResponseEntity atualizarHorarioFuncionamento(@PathVariable Long quadraId, @RequestBody @Valid HorarioFuncionamentoAtualizacao horarioFuncionamentoAtualizacao, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        var horarioFuncionamento = this.horarioFuncionamentoSerivce
                .atualizar(quadraId, horarioFuncionamentoAtualizacao, gestor);
        return ResponseEntity.ok(horarioFuncionamento);
    }

    @DeleteMapping("/{quadraId}/horarios-funcionamento")
    @Operation(summary = "Deleta um horário de funcionamento", description = "Deleta um horário de funcionamento para quadra que atenda o id no sistema")
    public ResponseEntity deletarHorarioFuncionamento(@PathVariable Long quadraId, @RequestParam DiaSemana diaSemana, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        this.horarioFuncionamentoSerivce.deletarFuncionamento(quadraId, diaSemana, gestor);
        return ResponseEntity.noContent().build();
    }

    // quadra-indisponivel
    @PostMapping("/{quadraId}/indisponibilidades")
    @Operation(summary = "Registra uma indisponibilidade", description = "Registra uma indisponibilidade de uso da quadra em determinada data para quadra que atenda o id no sistema")
    public ResponseEntity indicarQuadraComoIndisponivel(@PathVariable Long quadraId, @RequestBody @Valid IndisponibilidadeRegistro indisponibilidadeRegistro, Authentication auth){
        var gestor = (Usuario) auth.getPrincipal();
        var indisponibilidade = this.indisponibilidadeService
                .indicarQuadraComoIndisponivelEmDataSelecionada(quadraId, indisponibilidadeRegistro, gestor);
        return ResponseEntity.ok(indisponibilidade);
    }
}
