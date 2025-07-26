package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.reserva.ReservaPesquisa;
import br.com.nat.quadralivre.domain.reserva.ReservaRegistro;
import br.com.nat.quadralivre.domain.reserva.ReservaService;
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
@RequestMapping("/reservas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Reserva Controller", description = "Gerencia as reservas do sistema.")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/disponiveis")
    @Operation(summary = "Buscar reservas disponíveis", description = "Busca reservas disponíveis para quadra e em determinada data.")
    public ResponseEntity exibirHorariosReserva(@RequestBody @Valid ReservaPesquisa reservaPesquisa){
        var reservas = this.reservaService.exibirReservasDisponiveis(reservaPesquisa);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    @Operation(summary = "Registra uma reserva", description = "Registra uma reserva no sistema.")
    public ResponseEntity registrarReserva(@RequestBody @Valid ReservaRegistro reservaRegistro, Authentication auth, UriComponentsBuilder uriBuilder){
        var usuario = (Usuario) auth.getPrincipal();
        var reserva = this.reservaService.registrarReserva(reservaRegistro, usuario);

        var uri = uriBuilder
                .path("/reservas/{id}")
                .buildAndExpand(reserva.id())
                .toUri();

        return ResponseEntity.created(uri).body(reserva);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca reserva", description = "Busca reserva no sistema.")
    public ResponseEntity exibirReserva(@PathVariable String id, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        var reserva = this.reservaService.buscarReserva(id, usuario);
        return ResponseEntity.ok(reserva);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar reserva", description = "Remove uma reserva no sistema.")
    public ResponseEntity deletarReserva(@PathVariable String id, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        this.reservaService.deletarReserva(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
