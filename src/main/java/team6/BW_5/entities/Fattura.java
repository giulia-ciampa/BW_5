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

    @Column(nullable = false)
    private long numero;

    @ManyToOne
    @JoinColumn(name = "stato_id", nullable = false)
    private StatoFattura stato;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    //COSTRUTTORE

    public Fattura(LocalDate data, double importo, StatoFattura stato, Cliente cliente) {
        this.data = data;
        this.importo = importo;
        this.stato = stato;
        this.cliente = cliente;
        this.numero = 0;
    }


}