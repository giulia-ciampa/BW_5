package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "ruolo_utente")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RuoloUtente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID ruoloId;

    @Column(name = "ruolo")
    private String nomeRuolo; //admin o user


    public RuoloUtente(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }


}
