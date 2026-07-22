package team6.BW_5.responseDTO;

import jakarta.validation.constraints.Email;

import java.util.UUID;

public record UtentePatchDTO(String username,
                             UUID id,
                             @Email(message = "Il formato dell'email non è valido")
                             String email,
                             String password,
                             String nome,
                             String cognome  ) {
}
