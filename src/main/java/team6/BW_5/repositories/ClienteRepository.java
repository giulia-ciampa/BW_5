package team6.BW_5.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Utente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {
    boolean existsByEmail(String email);

    boolean existsByRagioneSociale(String ragioneSociale);

    boolean existsByPartitaIva(String partitaIva);

    boolean existsByPec(String pec);

    Page<Cliente> findByUtente(Utente utente, Pageable pageable);


    Optional<Cliente> findByEmail(String email);
}
