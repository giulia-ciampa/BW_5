package team6.BW_5.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtenteRequestDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 3, max = 20, message = "Lo username deve essere compreso tra 3 e 50 caratteri")
        String username,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Il formato dell'email non è valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$", message = "la password deve contenere almeno un numero, una lettera maiuscola e deve contenere almeno 6 caratteri")
        String password,

        String nome,
        String cognome
) {
}

