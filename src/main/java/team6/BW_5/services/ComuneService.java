package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Provincia;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.RecordAlreadyExistsException;
import team6.BW_5.repositories.ComuneRepository;

import java.util.List;

@Service
public class ComuneService {

    public final ComuneRepository comuneRepository;
    public final ProvinciaService provinciaService;

    public ComuneService(ComuneRepository comuneRepository, ProvinciaService provinciaService) {
        this.comuneRepository = comuneRepository;
        this.provinciaService = provinciaService;
    }

    public Comune save(Comune comune) {
        if (comuneRepository.existsByDenominazioneAndProvincia(comune.getDenominazione(), comune.getProvincia()))
            throw new RecordAlreadyExistsException("Il comune denominato " + comune.getDenominazione() + " esiste già nella provincia " + comune.getProvincia().getNome());
        return comuneRepository.save(comune);
    }

    public long count() {
        return comuneRepository.count();
    }

    public Comune findByDenominazioneAndProvincia(String denominazione, String siglaProvincia) {
        Provincia provincia = provinciaService.findBySigla(siglaProvincia);
        return comuneRepository.findByDenominazioneAndProvincia(denominazione, provincia).orElseThrow(() -> new NotFoundException("Comune non trovato."));
    }


    public List<Comune> findBySiglaProvincia(String siglaProvincia) {
        Provincia provincia = provinciaService.findBySigla(siglaProvincia);
        return comuneRepository.findByProvincia(provincia);
    }
}
