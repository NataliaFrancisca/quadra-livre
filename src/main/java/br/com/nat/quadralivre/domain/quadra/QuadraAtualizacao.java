package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.EnderecoRegistro;
import jakarta.validation.constraints.NotBlank;

public record QuadraAtualizacao (
        @NotBlank
        Long id,
        String nome,
        EnderecoRegistro endereco
){
}
