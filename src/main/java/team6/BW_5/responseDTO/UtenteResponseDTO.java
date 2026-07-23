package team6.BW_5.responseDTO;

import java.util.UUID;

public record UtenteResponseDTO(
        UUID id,
        String username,
        String email,
        String nome,
        String cognome
) {
}