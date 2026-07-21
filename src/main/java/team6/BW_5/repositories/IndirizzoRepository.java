package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Indirizzo;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {
    @Query("SELECT i FROM Indirizzo i " +
            "WHERE LOWER(i.via) = LOWER(:via) " +
            "AND LOWER(i.civico) = LOWER(:civico) " +
            "AND (:localita IS NULL OR LOWER(i.localita) = LOWER(:localita)) " +
            "AND LOWER(i.cap) = LOWER(:cap) " +
            "AND i.comune = :comune")
    Optional<Indirizzo> findByViaCivicoLocalitaOptionalAndComune(
            @Param("via") String via,
            @Param("civico") String civico,
            @Param("localita") String localita,
            @Param("cap") String cap,
            @Param("comune") Comune comune);
}
