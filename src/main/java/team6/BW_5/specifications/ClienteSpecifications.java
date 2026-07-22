package team6.BW_5.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Cliente;
import team6.BW_5.requestDTO.ClienteFilterDTO;

import java.time.LocalDate;

@Component
public class ClienteSpecifications {

    public Specification<Cliente> specificationClienteBuilder(ClienteFilterDTO filters) {
        Specification<Cliente> spec = (root, query, cb) -> cb.conjunction();
        if (filters.ragioneSociale() != null && !filters.ragioneSociale().isBlank()) {
            spec = spec.and(hasRagioneSociale(filters.ragioneSociale()));
        }

        if (filters.fatturatoMassimo() != null && !filters.fatturatoMassimo().isNaN()) {
            spec = spec.and(fatturatoLessThanOrEqualTo(filters.fatturatoMassimo()));
        }

        if (filters.fatturatoMinimo() != null && !filters.fatturatoMinimo().isNaN()) {
            spec = spec.and(fatturatoGreaterThanOrEqualTo(filters.fatturatoMinimo()));
        }

        if (filters.dataInserimentoMax() != null) {
            spec = spec.and(dataInserimentoBeforeThan(filters.dataInserimentoMax()));
        }

        if (filters.dataInserimentoMin() != null) {
            spec = spec.and(dataInserimentoAfterThan(filters.dataInserimentoMin()));
        }

        if (filters.dataUltimoContattoMax() != null) {
            spec = spec.and(dataUltimoContattoBeforeThan(filters.dataUltimoContattoMax()));
        }

        if (filters.dataUltimoContattoMin() != null) {
            spec = spec.and(dataUltimoContattoAfterThan(filters.dataUltimoContattoMin()));
        }
        return spec;
    }

    public Specification<Cliente> hasRagioneSociale(String ragioneSociale) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("ragioneSociale")),
                        "%" + ragioneSociale.toLowerCase() + "%"
                );
    }


    public Specification<Cliente> fatturatoGreaterThanOrEqualTo(Double valore) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("fatturatoAnnuale"),
                        valore
                );
    }

    public Specification<Cliente> fatturatoLessThanOrEqualTo(Double valore) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("fatturatoAnnuale"),
                        valore
                );
    }

    public Specification<Cliente> dataInserimentoBeforeThan(LocalDate dataInserimentoMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataInserimento"), dataInserimentoMax);
    }

    public Specification<Cliente> dataInserimentoAfterThan(LocalDate dataInserimentoMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataInserimento"), dataInserimentoMin);
    }

    public Specification<Cliente> dataUltimoContattoBeforeThan(LocalDate dataUltimoContattoMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataInserimento"), dataUltimoContattoMax);
    }

    public Specification<Cliente> dataUltimoContattoAfterThan(LocalDate dataUltimoContattoMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataInserimento"), dataUltimoContattoMin);
    }


}