package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.EnderecoRegistro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuadraAtualizacao (
        @NotNull
        Long id,
        String nome,
        EnderecoRegistro endereco
){
}
