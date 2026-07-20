package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "sedi")
public class Sede {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "sede_id")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "indirizzo_id", nullable = false)
    private Indirizzo indirizzo;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sede", nullable = false)
    private TipoSede tipo;

    public Sede(Indirizzo indirizzo, Cliente cliente, TipoSede tipo) {
        this.indirizzo = indirizzo;
        this.cliente = cliente;
        this.tipo = tipo;
    }
}
