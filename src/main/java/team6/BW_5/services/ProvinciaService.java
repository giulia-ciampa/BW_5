package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Provincia;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.ProvinciaRepository;

@Service
public class ProvinciaService {

    private final ProvinciaRepository provinciaRepository;


    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    public Provincia findBySigla(String sigla) {
        return provinciaRepository.findBySigla(sigla).orElseThrow(() -> new NotFoundException("Provincia non trovata."));
    }
}
