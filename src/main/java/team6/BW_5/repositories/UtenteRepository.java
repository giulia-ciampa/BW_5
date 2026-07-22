package team6.BW_5.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.Utente;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    // tutti gli utenti attivi
    Page<Utente> findByIsAttivoTrue(Pageable pageable);
    //trovo utente attivo per email e username (per login)
    Optional<Utente> findByEmailAndIsAttivoTrue(String email);
    Optional<Utente> findByUsernameAndIsAttivoTrue(String username);
    // metodo per cercare tramite username
    Optional<Utente> findByUsername(String username);
    // cerca per email
    Optional<Utente> findByEmail(String email);
    // esiste dato l'username
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
