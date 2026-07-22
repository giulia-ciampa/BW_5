package team6.BW_5.requestDTO;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaFilterDTO(
        UUID clienteId,

        String statoFattura,

        @PastOrPresent
        LocalDate dataInizio,

        @PastOrPresent
        LocalDate dataFine,


        Integer anno,

        @PositiveOrZero
        Double importoMin,

        @PositiveOrZero
        Double importoMax
) {
}
