package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Cliente;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByEmail(String email);

    boolean existsByRagioneSociale(String ragioneSociale);

    boolean existsByPartitaIva(String partitaIva);

    boolean existsByPec(String pec);
}
