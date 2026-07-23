package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Indirizzo;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {
    Optional<Indirizzo> findByViaIgnoreCaseAndCivicoIgnoreCaseAndCapIgnoreCaseAndComune(
            String via,
            String civico,
            String cap,
            Comune comune
    );

    Optional<Indirizzo> findByViaIgnoreCaseAndCivicoIgnoreCaseAndLocalitaIgnoreCaseAndCapIgnoreCaseAndComune(
            String via,
            String civico,
            String localita,
            String cap,
            Comune comune
    );
}
