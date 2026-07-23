package team6.BW_5.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Fattura;
import team6.BW_5.requestDTO.FatturaFilterDTO;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class FatturaSpecifications {
    public Specification<Fattura> specificationFatturaBuilder(FatturaFilterDTO filters) {
        Specification<Fattura> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (filters.clienteId() != null)
            spec = spec.and(hasClienteId(filters.clienteId()));

        if (filters.statoFattura() != null) {
            spec = spec.and(hasStato(filters.statoFattura()));
        }

        if (filters.anno() != null) {
            spec = spec.and(hasAnno(filters.anno()));
        }

        if (filters.dataInizio() != null) {
            spec = spec.and(dataInizioAfterThan(filters.dataInizio()));
        }

        if (filters.dataFine() != null) {
            spec = spec.and(dataFineBeforeThan(filters.dataFine()));
        }

        if (filters.importoMin() != null && !filters.importoMin().isNaN()) {
            spec = spec.and(importoGreaterThanOrEqualTo(filters.importoMin()));
        }

        if (filters.importoMax() != null && !filters.importoMax().isNaN()) {
            spec = spec.and(importoLessThanOrEqualTo(filters.importoMax()));
        }

        return spec;
    }

    // --- SPECIFICATIONS ---

    public Specification<Fattura> hasClienteId(UUID clienteId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cliente").get("id"), clienteId));
    }

    public Specification<Fattura> hasStato(String statoFattura) {
        return (root, query, cb) -> {
            if (statoFattura == null || statoFattura.isBlank()) return null;


            return cb.equal(cb.upper(root.join("stato").get("stato")), statoFattura.toUpperCase());
        };
    }

    public Specification<Fattura> hasAnno(Integer anno) {
        return (root, query, cb) -> {
            if (anno == null) return null;
            LocalDate inizioAnno = LocalDate.of(anno, 1, 1);
            LocalDate fineAnno = LocalDate.of(anno, 12, 31);
            return cb.between(root.get("data"), inizioAnno, fineAnno);
        };
    }

    public Specification<Fattura> dataInizioAfterThan(LocalDate dataInizio) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("data"), dataInizio);
    }

    public Specification<Fattura> dataFineBeforeThan(LocalDate dataFine) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("data"), dataFine);
    }

    public Specification<Fattura> importoGreaterThanOrEqualTo(Double importoMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("importo"), importoMin);
    }

    public Specification<Fattura> importoLessThanOrEqualTo(Double importoMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("importo"), importoMax);
    }

}
