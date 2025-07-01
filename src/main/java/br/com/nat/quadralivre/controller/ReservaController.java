package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.reserva.ReservaBusca;
import br.com.nat.quadralivre.domain.reserva.ReservaRegistro;
import br.com.nat.quadralivre.domain.reserva.ReservaService;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    private URI generateURI(String id, UriComponentsBuilder uriBuilder){
        return uriBuilder
                .path("/reservas/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    @GetMapping("/livres")
    public ResponseEntity exibirHorariosReserva(@RequestBody @Valid ReservaBusca busca){
        var reservas = this.reservaService.exibirReservas(busca);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity exibirReserva(@PathVariable String id, Authentication authentication){
        var usuario = (Usuario) authentication.getPrincipal();
        var reserva = this.reservaService.buscarReserva(id, usuario);
        return ResponseEntity.ok(reserva);
    }


    @PostMapping
    public ResponseEntity registrarReserva(@RequestBody @Valid ReservaRegistro reserva, Authentication authentication, UriComponentsBuilder uri){
        var usuario = (Usuario) authentication.getPrincipal();
        var dadosReserva = this.reservaService.registrarReserva(reserva, usuario);
        return ResponseEntity.created(this.generateURI(dadosReserva.id(), uri)).body(dadosReserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarReserva(@PathVariable String id, Authentication authentication){
        var usuario = (Usuario) authentication.getPrincipal();

        this.reservaService.deletarReserva(id, usuario);

        return ResponseEntity.noContent().build();
    }
}
