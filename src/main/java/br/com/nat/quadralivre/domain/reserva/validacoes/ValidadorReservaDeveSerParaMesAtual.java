package br.com.nat.quadralivre.domain.reserva.validacoes;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class ValidadorReservaDeveSerParaMesAtual {

    public void validar(LocalDate dataParReserva){
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataReserva = dataParReserva.withDayOfMonth(1);

        if (dataAtual.getMonth().equals(dataReserva.getMonth())){
            return;
        }

        boolean ultimoDiaDoMes = dataAtual.getDayOfMonth() == dataAtual.lengthOfMonth();

        if (ultimoDiaDoMes){
            return;
        }

        throw new IllegalArgumentException("Reservas para meses futuros só são liberados no último dia do mês anterior.");
    }
}
