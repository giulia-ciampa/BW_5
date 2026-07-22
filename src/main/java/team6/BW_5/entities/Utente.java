package team6.BW_5.entities;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID utenteId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    private String avatar;
    private boolean isAttivo;

    @OneToMany(
            mappedBy = "utente",
            fetch = FetchType.EAGER
    )
    private List<AssegnazioneRuolo> assegnazioniRuolo = new ArrayList<>();

    public Utente(String username, String email, String password, String nome, String cognome,  boolean isAttivo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
        this.isAttivo = isAttivo;
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