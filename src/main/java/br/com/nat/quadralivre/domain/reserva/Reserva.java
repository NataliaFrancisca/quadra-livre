package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "Reserva")
@Table(name = "reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reserva {
    @Id
    @Column(unique = true)
    private String id;

    LocalTime abertura;
    LocalTime encerramento;
    LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "quadra_id", nullable = false)
    Quadra quadra;

    public Reserva(ReservaDisponivel reservaDisponivel){
        this.setId(reservaDisponivel.id());
        this.setAbertura(reservaDisponivel.abertura());
        this.setEncerramento(reservaDisponivel.fechamento());
        this.setData(reservaDisponivel.data());
    }
}
