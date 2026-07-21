package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "clienti")
public class Cliente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "cliente_id")
    private UUID idCliente;
    @Column(name = "ragione_sociale", nullable = false, unique = true)
    private String ragioneSociale;
    @Column(name = "partita_iva", nullable = false, unique = true)
    private String partitaIva;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, name = "data_inserimento")
    private LocalDate dataInserimento;
    @Column(nullable = false, name = "data_ultimo_contatto")
    private LocalDate dataUltimoContatto;
    @Column(nullable = false, name = "fatturato_annuale")
    private double fatturatoAnnuale;
    @Column(nullable = false, unique = true)
    private String pec;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false, name = "email_contatto")
    private String emailContatto;
    @Column(nullable = false, name = "nome_contatto")
    private String nomeContatto;
    @Column(nullable = false, name = "cognome_contatto")
    private String cognomeContatto;
    @Column(nullable = false, name = "telefono_contatto")
    private String telefonoContatto;
    @Column(nullable = false, name = "logo_aziendale")
    private String logoAziendale;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAzienda tipo;
    @ManyToOne
    @JoinColumn(name = "creato_da", nullable = false)
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "sede_legale", nullable = false)
    private Indirizzo sedeLegale;
    @ManyToOne
    @JoinColumn(name = "sede_operativa", nullable = false)
    private Indirizzo sedeOperativa;


    public Cliente(String ragioneSociale, String partitaIva, String email, double fatturatoAnnuale, String pec, String telefono, String emailContatto, String nomeContatto, String cognomeContatto, String telefonoContatto, TipoAzienda tipo, Utente utente, Indirizzo sedeLegale, Indirizzo sedeOperativa) {
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.email = email;
        this.fatturatoAnnuale = fatturatoAnnuale;
        this.pec = pec;
        this.telefono = telefono;
        this.emailContatto = emailContatto;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.telefonoContatto = telefonoContatto;
        this.tipo = tipo;
        this.utente = utente;
        this.dataInserimento = LocalDate.now();
        this.dataUltimoContatto = LocalDate.now();
        this.logoAziendale = "https://ui-avatars.com/api/?name=" + ragioneSociale;
        this.sedeLegale = sedeLegale;
        this.sedeOperativa = sedeOperativa;
    }
}
