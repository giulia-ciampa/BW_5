package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.BW_5.entities.Fattura;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    Optional<Fattura> findFirstByDataBetweenOrderByNumeroDesc(LocalDate dataInizio, LocalDate dataFine);
}
