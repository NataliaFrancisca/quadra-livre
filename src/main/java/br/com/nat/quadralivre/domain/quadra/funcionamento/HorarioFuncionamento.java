package br.com.nat.quadralivre.domain.quadra.funcionamento;

import br.com.nat.quadralivre.domain.quadra.Quadra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity(name = "HorarioFuncionamento")
@Table(name = "horario_funcionamento", uniqueConstraints = {@UniqueConstraint(columnNames = {"quadra_id", "dia"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioFuncionamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;

    @Column(nullable = false)
    private LocalTime abertura;
    @Column(nullable = false)
    private LocalTime fechamento;
    private boolean disponibilidade;

    @ManyToOne
    @JoinColumn(name = "quadra_id")
    private Quadra quadra;

    public HorarioFuncionamento(HorarioFuncionamentoRegistro horarioFuncionamentoRegistro){
        this.dia = horarioFuncionamentoRegistro.diaSemana();
        this.abertura = horarioFuncionamentoRegistro.abertura();
        this.fechamento = horarioFuncionamentoRegistro.fechamento();
        this.disponibilidade = true;
    }

    public void atualizar(HorarioFuncionamentoAtualizacao horarioFuncionamentoAtualizacao){

        if (horarioFuncionamentoAtualizacao.abertura() != null){
            this.abertura = horarioFuncionamentoAtualizacao.abertura();
        }

        if (horarioFuncionamentoAtualizacao.fechamento() != null){
            this.fechamento = horarioFuncionamentoAtualizacao.fechamento();
        }

        if (horarioFuncionamentoAtualizacao.disponibilidade() != null){
            this.disponibilidade = horarioFuncionamentoAtualizacao.disponibilidade();
        }
    }
}