package team6.BW_5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "province")
public class Provincia {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "provincia_id")
    private UUID id;
    @Column(nullable = false, unique = true)
    private String sigla;
    @Column(nullable = false, unique = true)
    private String nome;
    @Column(nullable = false)
    private String regione;

    public Provincia(String sigla, String nome, String regione) {
        this.sigla = sigla;
        this.nome = nome;
        this.regione = regione;
    }
}
