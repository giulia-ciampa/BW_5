package team6.BW_5.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import team6.BW_5.entities.Fattura;
import team6.BW_5.entities.StatoFattura;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID>, JpaSpecificationExecutor<Fattura> {
    Optional<Fattura> findFirstByDataBetweenOrderByNumeroDesc(LocalDate dataInizio, LocalDate dataFine);

    Page<Fattura> findByStato(StatoFattura stato, Pageable pageable);
}
