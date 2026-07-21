package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Provincia;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.ComuneRepository;

@Service
public class ComuneService {

    public final ComuneRepository comuneRepository;
    public final ProvinciaService provinciaService;

    public ComuneService(ComuneRepository comuneRepository, ProvinciaService provinciaService) {
        this.comuneRepository = comuneRepository;
        this.provinciaService = provinciaService;
    }

    public Comune findByDenominazioneAndProvincia(String denominazione, String siglaProvincia) {
        Provincia provincia = provinciaService.findBySigla(siglaProvincia);
        return comuneRepository.findByDenominazioneAndProvincia(denominazione, provincia).orElseThrow(() -> new NotFoundException("Comune non trovato."));
    }
}
