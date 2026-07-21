package team6.BW_5.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaDTO(
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "la data è obbligatoria")
        @PastOrPresent(message = "La data della fattura non può essere nel futuro")
        LocalDate data,

        @NotNull(message = "l'importo è obbligatorio")
        @Positive(message = "L'importo della fattura deve essere maggiore di zero")
        Double importo,

        @NotNull(message = "L'ID del cliente è obbligatorio")
        UUID idCliente) {
}
