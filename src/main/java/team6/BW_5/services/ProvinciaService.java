package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Provincia;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.RecordAlreadyExistsException;
import team6.BW_5.repositories.ProvinciaRepository;

import java.util.List;

@Service
public class ProvinciaService {

    private final ProvinciaRepository provinciaRepository;


    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    public List<Provincia> findAll() {
        return provinciaRepository.findAll();
    }

    public Provincia save(Provincia provincia) {
        if (provinciaRepository.existsBySigla(provincia.getSigla()))
            throw new RecordAlreadyExistsException("La provincia con sigla " + provincia.getSigla() + " esiste già");
        return provinciaRepository.save(provincia);
    }

    public long count() {
        return provinciaRepository.count();
    }

    public Provincia findBySigla(String sigla) {
        return provinciaRepository.findBySigla(sigla).orElseThrow(() -> new NotFoundException("Provincia non trovata."));
    }

}
