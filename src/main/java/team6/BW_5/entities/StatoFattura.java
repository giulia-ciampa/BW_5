package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "stato_fatture")
@ToString
@Getter
@Setter
public class StatoFattura {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID statoFatturaId;

    @Column(nullable = false)
    private String stato;

    //COSTRUTTORE
    public StatoFattura(String stato) {
        this.stato = stato;
    }
}
