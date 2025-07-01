package br.com.nat.quadralivre.domain.quadra.funcionamento;

public enum DiaSemana {
    DOMINGO ("SUNDAY"),
    SEGUNDA ("MONDAY"),
    TERCA ("TUESDAY"),
    QUARTA ("WEDNESDAY"),
    QUINTA ("THURSDAY"),
    SEXTA ("FRIDAY"),
    SABADO ("SATURDAY");

    private String diaSemanaEmIngles;

    DiaSemana(String diaSemanaEmIngles){
        this.diaSemanaEmIngles = diaSemanaEmIngles;
    }

    public static DiaSemana fromEnglish(String diaSemanaEmIngles){
        for (DiaSemana diaSemana : DiaSemana.values()){
            if (diaSemana.diaSemanaEmIngles.equals(diaSemanaEmIngles)){
                return diaSemana;
            }
        }

        throw new IllegalArgumentException("Esse dia da semana não é compativel.");
    }
}
