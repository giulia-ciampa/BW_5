package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Provincia;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, UUID> {
    Optional<Comune> findByDenominazioneAndProvincia(String denominazione, Provincia provincia);
}
