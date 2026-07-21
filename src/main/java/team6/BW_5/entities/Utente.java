package team6.BW_5.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Lo username è obbligatorio!")
    @Size(min = 3, max = 20, message = "Lo username deve avere una lunghezza compresa tra 3 e 20 caratteri!")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Il formato dell'email non è valido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Il nome è obbligatorio")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Column(nullable = false)
    private String cognome;

    private String avatar;

    @OneToMany(
            mappedBy = "utente",
            fetch = FetchType.LAZY
    )
    private List<AssegnazioneRuolo> assegnazioniRuolo = new ArrayList<>();

    public Utente(String username, String email, String password, String nome, String cognome) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return assegnazioniRuolo.stream()
                .filter(assegnazione -> assegnazione.getDataRevoca() == null)
                .map(AssegnazioneRuolo::getRuolo)
                .map(RuoloUtente::getNomeRuolo)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    

}