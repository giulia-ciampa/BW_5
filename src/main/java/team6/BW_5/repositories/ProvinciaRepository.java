package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.Provincia;

import java.util.Optional;
import java.util.UUID;

public interface ProvinciaRepository extends JpaRepository<Provincia, UUID> {
    Optional<Provincia> findBySigla(String sigla);
}
