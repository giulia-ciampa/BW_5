package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@ToString
@NoArgsConstructor
@Table(name = "fatture")
@Getter
@Setter

public class Fattura {

    //ATTRIBUTI
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private double importo;

    @Column(nullable = false, unique = true)
    private long numero;

    @JoinColumn(name = "stato_id", nullable = false)
    @ManyToOne
    private StatoFattura stato;

    //JoinColumn(name="id_cliente", nullable=false)
    //@ManyToOne
    //private Cliente cliente;

    //COSTRUTTORE

    public Fattura(LocalDate data, double importo, long numero, StatoFattura statoFattura) {
    }


}