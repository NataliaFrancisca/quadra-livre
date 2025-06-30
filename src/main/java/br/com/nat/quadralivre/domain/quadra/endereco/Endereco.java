package br.com.nat.quadralivre.domain.quadra.endereco;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {
    @Column(nullable = false)
    private String local;
    @Column(nullable = false)
    private String rua;
    @Column(nullable = false)
    private String numero;
    @Column(nullable = false)
    private String bairro;
    @Column(nullable = false)
    private String cidade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    public void atualizar(EnderecoRegistro endereco){
        if (endereco.local() != null){
            this.local = endereco.local();
        }

        if (endereco.rua() != null){
            this.local = endereco.rua();
        }

        if (endereco.numero() != null){
            this.numero = endereco.numero();
        }

        if (endereco.bairro() != null){
            this.bairro = endereco.bairro();
        }

        if (endereco.cidade() != null){
            this.cidade = endereco.cidade();
        }

        if (endereco.estado() != null){
            this.estado = endereco.estado();
        }
    }
}
