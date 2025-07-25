package br.com.nat.quadralivre.domain.quadra.indisponibilidade;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Indisponibilidade")
@Table(name = "indisponibilidades")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Indisponibilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    private Quadra quadra;

    public Indisponibilidade(IndisponibilidadeRegistro registro){
        this.data = registro.data();
    }
}
