package br.com.nat.quadralivre.domain.reserva;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
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

    private LocalTime inicio;
    private LocalTime encerramento;
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "quadra_id", nullable = false)
    private Quadra quadra;

    public Reserva(ReservaDisponivel reservaDisponivel){
        this.setId(reservaDisponivel.getId());
        this.setInicio(reservaDisponivel.getInicio());
        this.setEncerramento(reservaDisponivel.getEncerramento());
        this.setData(reservaDisponivel.getData());
        this.diaSemana = DiaSemana.fromEnglish(reservaDisponivel.getData().getDayOfWeek().name());
    }
}
