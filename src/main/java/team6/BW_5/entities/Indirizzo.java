package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "indirizzi")
public class Indirizzo {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "indirizzo_id")
    private UUID id;
    @Column(nullable = false)
    private String via;
    @Column(nullable = false)
    private String civico;
    private String localita;
    @Column(nullable = false)
    private String cap;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Comune comune;

    public Indirizzo(String via, String civico, String localita, String cap, Comune comune) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
    }
}
