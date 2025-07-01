package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.Endereco;
import br.com.nat.quadralivre.domain.quadra.funcionamento.HorarioFuncionamento;
import br.com.nat.quadralivre.domain.reserva.Reserva;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Quadra")
@Table(name = "quadras")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Quadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Embedded
    @Column(nullable = false)
    private Endereco endereco;

    @ManyToOne
    private Usuario gestor;

    @OneToMany(mappedBy = "quadra", cascade = CascadeType.ALL)
    private List<HorarioFuncionamento> horarios;

    @OneToMany(mappedBy = "quadra")
    private List<Reserva> reservas;

    public Quadra(QuadraRegistro quadraRegistro) {
        this.nome = quadraRegistro.nome();
        this.endereco = quadraRegistro.endereco();
    }

    public void atualizar(QuadraAtualizacao quadraAtualizacao){
        if (quadraAtualizacao.nome() != null){
            this.nome = quadraAtualizacao.nome();;
        }

        if (quadraAtualizacao.endereco() != null){
            this.endereco.atualizar(quadraAtualizacao.endereco());
        }
    }
}
