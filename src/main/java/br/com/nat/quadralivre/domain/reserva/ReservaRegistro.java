package br.com.nat.quadralivre.domain.reserva;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaRegistro(
        @NotNull(message = "O campo de ID da quadra é obrigatório.")
        Long quadraId,
        @NotNull
        @FutureOrPresent(message = "Digite uma data válida. Datas no presente ou futuro.")
        LocalDate data,
        @NotNull(message = "O campo de ID da reserva é obrigatório.")
        String reservaId
) {
}
