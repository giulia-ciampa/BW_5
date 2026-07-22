package team6.BW_5.responseDTO;

import jakarta.validation.constraints.Email;

public record UtentePatchDTO(String username,
                             @Email(message = "Il formato dell'email non è valido")
                             String email,
                             String password,
                             String nome,
                             String cognome  ) {
}
