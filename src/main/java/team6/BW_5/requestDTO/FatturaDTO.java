package team6.BW_5.requestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaDTO(
        @PastOrPresent(message = "La data della fattura non può essere nel futuro")
        LocalDate data,

        @Positive(message = "L'importo della fattura deve essere maggiore di zero")
        double importo,

        @NotNull(message = "L'ID del cliente è obbligatorio")
        UUID idCliente) {
}
