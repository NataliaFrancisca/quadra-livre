package br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ValidadorHorarioAberturaFechamento {

    public void validar(LocalTime abertura, LocalTime fechamento){
        LocalDateTime aberturaA = LocalDate.now().atTime(abertura);
        LocalDateTime fechamentoB = LocalDate.now().atTime(fechamento);

        if (aberturaA.isAfter(fechamentoB)){
            throw new IllegalArgumentException("Horário de abertura deve ser antes do horário de encerramento.");
        }

        if (fechamentoB.isBefore(aberturaA)){
            throw new IllegalArgumentException("Horário de fechamento deve ser após o horário de abertura.");
        }
    }
}
