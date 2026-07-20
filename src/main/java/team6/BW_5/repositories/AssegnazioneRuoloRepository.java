package team6.BW_5.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.AssegnazioneRuolo;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.entities.Utente;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssegnazioneRuoloRepository extends JpaRepository<AssegnazioneRuolo, UUID> {

    // storico della assegnazioni per utente con paginazione per il getmapping
    Page<AssegnazioneRuolo> findByUtente(Utente utente, Pageable pageable);

    // trova se esiste già un'assegnazione attiva   per un utente e un ruolo specifico
    Optional<AssegnazioneRuolo> findByUtenteAndRuoloAndDataRevocaIsNull(Utente utente, RuoloUtente ruolo);
}