package team6.BW_5.requestDTO;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaPatchDTO(
        @PastOrPresent(message = "la data non può essere nel futuro")
        LocalDate data,

        @Positive(message = "L'importo della fattura deve essere maggiore di zero")
        Double importo,

        UUID idCliente) {

}
