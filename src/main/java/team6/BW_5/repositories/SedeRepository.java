package team6.BW_5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team6.BW_5.entities.Sede;

import java.util.UUID;

@Repository
public interface SedeRepository extends JpaRepository<Sede, UUID> {
}
