package team6.BW_5.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "assegnazione_ruolo")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AssegnazioneRuolo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_ruolo")
    private RuoloUtente ruolo;

    @Column(name = "data_assegnazione")
    private LocalDate dataAssegnazione;

    @Column(name = "data_revoca")
    private LocalDate dataRevoca;

    public AssegnazioneRuolo(Utente utente, RuoloUtente ruolo, LocalDate dataAssegnazione) {
        this.utente = utente;
        this.ruolo = ruolo;
        this.dataAssegnazione = dataAssegnazione;
        //data revoca parte a null
    }
}