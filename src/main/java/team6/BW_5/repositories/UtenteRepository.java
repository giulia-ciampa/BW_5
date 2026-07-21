package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.Utente;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    // metodo per cercare tramite username
    Optional<Utente> findByUsername(String username);
    // cerca per email
    Optional<Utente> findByEmail(String email);
    // esiste dato l'username
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
