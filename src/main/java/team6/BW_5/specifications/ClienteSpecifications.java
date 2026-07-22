package team6.BW_5.specifications;

import org.springframework.data.jpa.domain.Specification;
import team6.BW_5.entities.Cliente;

import java.time.LocalDate;

public class ClienteSpecifications {

    public static Specification<Cliente> hasRagioneSociale(String ragioneSociale) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("ragioneSociale")),
                        "%" + ragioneSociale.toLowerCase() + "%"
                );
    }


    public static Specification<Cliente> fatturatoGreaterThanOrEqualTo(Double valore) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("fatturatoAnnuale"),
                        valore
                );
    }

    public static Specification<Cliente> fatturatoLessThanOrEqualTo(Double valore) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("fatturatoAnnuale"),
                        valore
                );
    }

    public static Specification<Cliente> dataInserimentoBeforeThan(LocalDate dataInserimentoMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataInserimento"), dataInserimentoMax);
    }
}