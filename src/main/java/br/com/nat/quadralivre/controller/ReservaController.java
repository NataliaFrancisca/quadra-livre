package br.com.nat.quadralivre.controller;

import br.com.nat.quadralivre.domain.reserva.ReservaPesquisa;
import br.com.nat.quadralivre.domain.reserva.ReservaRegistro;
import br.com.nat.quadralivre.domain.reserva.ReservaService;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity exibirHorariosReserva(@RequestBody @Valid ReservaPesquisa reservaPesquisa){
        var reservas = this.reservaService.exibirReservasDisponiveis(reservaPesquisa);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
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
    public ResponseEntity exibirReserva(@PathVariable String id, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        var reserva = this.reservaService.buscarReserva(id, usuario);
        return ResponseEntity.ok(reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarReserva(@PathVariable String id, Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();
        this.reservaService.deletarReserva(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
