package team6.BW_5.requestDTO;

import jakarta.validation.constraints.NotNull;

public record PatchAttivazioneClienteDTO(
        @NotNull(message = "Attivazione deve essere true o false")
        boolean isAttivo
) {
}
