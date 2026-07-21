package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.RuoloUtente;

import java.util.Optional;
import java.util.UUID;

public interface RuoloUtenteRepository extends JpaRepository<RuoloUtente, UUID> {
    Optional<RuoloUtente> findByNomeRuolo(String nomeRuolo);
}
