package br.com.nat.quadralivre.domain.quadra;

import br.com.nat.quadralivre.domain.quadra.endereco.EnderecoRegistro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QuadraAtualizacao (
        @NotNull(message = "O campo de ID da quadra é obrigatório.")
        Long id,
        @Size(min = 4, message = "O nome deve ter pelo menos 4 caracteres.")
        String nome,
        EnderecoRegistro endereco
){
}
