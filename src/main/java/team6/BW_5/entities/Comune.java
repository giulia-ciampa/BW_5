package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "comuni")
public class Comune {
    @ManyToOne
    @JoinColumn(nullable = false)
    Provincia provincia;
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "comune_id")
    private UUID comuneId;
    @Column(name = "codice_provincia", nullable = false)
    private int codiceProvincia;
    @Column(name = "progressivo_comune", nullable = false)
    private int progressivoComune;
    @Column(nullable = false)
    private String denominazione;

    public Comune(int codiceProvincia, int progressivoComune, String denominazione, Provincia provincia) {
        this.codiceProvincia = codiceProvincia;
        this.progressivoComune = progressivoComune;
        this.denominazione = denominazione;
        this.provincia = provincia;
    }
}
