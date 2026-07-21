package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.StatoFattura;

import java.util.Optional;
import java.util.UUID;

public interface StatoFatturaRepository extends JpaRepository<StatoFattura, UUID> {
    Optional<StatoFattura> findByStato(String stato);
}
