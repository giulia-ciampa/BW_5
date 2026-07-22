package team6.BW_5.requestDTO;

import jakarta.validation.constraints.NotBlank;

public record StatoFatturaDTO(@NotBlank String nuovoStato) {
}
