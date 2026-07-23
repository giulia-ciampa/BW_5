package team6.BW_5.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IndirizzoDTO(
        @NotBlank
        @Size(min = 3)
        String via,
        @NotBlank
        @Size
        String civico,
        String localita,
        @NotBlank
        @Size(min = 3)
        String cap,
        @NotBlank
        @Size(min = 3)
        String siglaProvincia,
        @NotBlank
        @Size(min = 3)
        String denominazioneComune
) {
}
