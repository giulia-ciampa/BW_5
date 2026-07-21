package team6.BW_5.requestDTO;

import jakarta.validation.constraints.*;
import team6.BW_5.entities.TipoAzienda;

public record ClienteDTO(
        @NotBlank(message = "Il campo ragione sociale deve essere compilato.")
        @Size(min = 3, message = "Il campo ragione sociale deve essere lungo almeno 3 caratteri.")
        String ragioneSociale,
        @NotBlank(message = "Il campo P.IVA deve essere compilato.")
        @Size(min = 11, max = 11, message = "Il campo P.IVA deve essere lungo 11 caratteri.")
        String partitaIva,
        @NotBlank(message = "Il campo email deve essere compilato.")
        @Email(message = "Il campo email deve essere un'email valida")
        String email,
        @PositiveOrZero(message = "Il campo fatturato annuale deve essere maggiore o uguale a zero.")
        @NotNull(message = "Il campo fatturato annuale deve essere compilato.")
        double fatturatoAnnuale,
        @NotBlank(message = "Il campo pec deve essere compilato.")
        @Email(message = "Il campo pec deve essere una pec valida")
        String pec,
        @NotBlank(message = "Il campo telefono deve essere compilato.")
        @Size(min = 9, message = "Il campo telefono deve essere lungo almeno 9 caratteri.")
        String telefono,
        @NotBlank(message = "Il campo email di contatto deve essere compilato.")
        @Email(message = "Il campo email di contatto deve essere un'email valida")
        String emailContatto,
        @NotBlank(message = "Il campo nome contatto deve essere compilato.")
        @Size(min = 3, message = "Il campo nome contatto deve essere lungo almeno 3 caratteri.")
        String nomeContatto,
        @NotBlank(message = "Il campo cognome contatto deve essere compilato.")
        @Size(min = 3, message = "Il campo cognome contatto deve essere lungo almeno 3 caratteri.")
        String cognomeContatto,
        @NotBlank(message = "Il campo telefono contatto deve essere compilato.")
        @Size(min = 9, message = "Il campo telefono contatto deve essere lungo almeno 9 caratteri.")
        String telefonoContatto,
        @NotNull(message = "Il campo email di contatto deve essere compilato.")
        TipoAzienda tipo
) {
}
