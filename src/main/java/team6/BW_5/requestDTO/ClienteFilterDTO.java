package team6.BW_5.requestDTO;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record ClienteFilterDTO(
        String ragioneSociale,
        @PositiveOrZero(message = "Il fatturato deve essere un numero positivo o zero")
        Double fatturatoMassimo,
        @PositiveOrZero(message = "Il fatturato deve essere un numero positivo o zero")
        Double fatturatoMinimo,
        @PastOrPresent(message = "Le date di inserimento o ultimo contatto non possono essere impostate nel futuro.")
        LocalDate dataInserimentoMax,
        @PastOrPresent(message = "Le date di inserimento o ultimo contatto non possono essere impostate nel futuro.")
        LocalDate dataInserimentoMin,
        @PastOrPresent(message = "Le date di inserimento o ultimo contatto non possono essere impostate nel futuro.")
        LocalDate dataUltimoContattoMax,
        @PastOrPresent(message = "Le date di inserimento o ultimo contatto non possono essere impostate nel futuro.")
        LocalDate dataUltimoContattoMin
) {
}
