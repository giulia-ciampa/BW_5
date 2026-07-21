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
        TipoAzienda tipo,
        @NotNull(message = "Il campo provincia sede legale deve essere compilato")
        String siglaProvinciaSedeLegale,
        @NotNull(message = "Il campo comune sede legale deve essere compilato")
        String denominazioneComuneSedeLegale,
        @NotBlank(message = "Il campo via sede legale deve essere compilato")
        @Size(min = 3, message = "Il campo via sede legale deve essere lungo almeno 3 caratteri.")
        String viaSedeLegale,
        @NotBlank(message = "Il campo civico sede legale deve essere compilato")
        @Size(min = 1, message = "Il campo civico sede legale deve essere lungo almeno 1 carattere.")
        String civicoSedeLegale,
        String localitaSedeLegale,
        @NotBlank(message = "Il campo CAP sede legale deve essere compilato")
        @Size(min = 4, message = "Il campo CAP sede legale deve essere lungo almeno 4 caratteri.")
        String capSedeLegale,
        @NotNull(message = "Il campo provincia sede operativa deve essere compilato")
        String siglaProvinciaSedeOperativa,
        @NotNull(message = "Il campo comune sede operativa deve essere compilato")
        String denominazioneComuneSedeOperativa,
        @NotBlank(message = "Il campo via sede operativa deve essere compilato")
        @Size(min = 3, message = "Il campo via sede operativa deve essere lungo almeno 3 caratteri.")
        String viaSedeOperativa,
        @NotBlank(message = "Il campo civico sede operativa deve essere compilato")
        @Size(min = 1, message = "Il campo civico sede operativa deve essere lungo almeno 1 carattere.")
        String civicoSedeOperativa,
        String localitaSedeOperativa,
        @NotBlank(message = "Il campo CAP sede operativa deve essere compilato")
        @Size(min = 4, message = "Il campo CAP sede operativa deve essere lungo almeno 4 caratteri.")
        String capSedeOperativa
) {
}
