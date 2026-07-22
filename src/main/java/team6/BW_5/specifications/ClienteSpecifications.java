package team6.BW_5.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Cliente;

import java.time.LocalDate;

@Component
public class ClienteSpecifications {

    public Specification<Cliente> specificationClienteBuilder(String ragioneSociale, Double fatturatoMassimo, Double fatturatoMinimo, LocalDate dataInserimentoMax, LocalDate dataInserimentoMin, LocalDate dataUltimoContattoMax, LocalDate dataUltimoContattoMin) {
        Specification<Cliente> spec = (root, query, cb) -> cb.conjunction();
        if (ragioneSociale != null && !ragioneSociale.isBlank()) {
            spec = spec.and(hasRagioneSociale(ragioneSociale));
        }

        if (fatturatoMassimo != null && !fatturatoMassimo.isNaN()) {
            spec = spec.and(fatturatoLessThanOrEqualTo(fatturatoMassimo));
        }

        if (fatturatoMinimo != null && !fatturatoMinimo.isNaN()) {
            spec = spec.and(fatturatoGreaterThanOrEqualTo(fatturatoMinimo));
        }

        if (dataInserimentoMax != null) {
            spec = spec.and(dataInserimentoBeforeThan(dataInserimentoMax));
        }

        if (dataInserimentoMin != null) {
            spec = spec.and(dataInserimentoAfterThan(dataInserimentoMin));
        }

        if (dataUltimoContattoMax != null) {
            spec = spec.and(dataUltimoContattoBeforeThan(dataUltimoContattoMax));
        }

        if (dataUltimoContattoMin != null) {
            spec = spec.and(dataUltimoContattoAfterThan(dataUltimoContattoMin));
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