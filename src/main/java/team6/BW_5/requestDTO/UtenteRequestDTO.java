package team6.BW_5.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtenteRequestDTO(
        @NotBlank(message = "Lo username è obbligatorio")
                               @Size(min = 3, max = 20, message = "Lo username deve essere compreso tra 3 e 50 caratteri")
                               String username,

                               @NotBlank(message = "L'email è obbligatoria")
                               @Email(message = "Il formato dell'email non è valido")
                               String email,

                               @NotBlank(message = "La password è obbligatoria")
                               @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
                               String password,

                               String nome,
                               String cognome
) {
}

