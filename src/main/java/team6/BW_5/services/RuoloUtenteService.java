package team6.BW_5.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.RecordAlreadyExistsException;
import team6.BW_5.repositories.RuoloUtenteRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RuoloUtenteService {
    @Autowired
    private RuoloUtenteRepository ruoloUtenteRepository;

    //metodo per creare e salvare un ruolo
    public RuoloUtente creaRuolo(String nomeRuolo) {
        if (ruoloUtenteRepository.findByNomeRuolo(nomeRuolo).isPresent()) {
            throw new RecordAlreadyExistsException("Il ruolo" + " " + nomeRuolo + " è già presente nel DB");
        }
        RuoloUtente nuovoRuolo = new RuoloUtente(nomeRuolo);
        return ruoloUtenteRepository.save(nuovoRuolo);
    }

    //metodo per recuperare i ruoli dal db
    public List<RuoloUtente> findAllRuoli() {
        return ruoloUtenteRepository.findAll();
    }

    //cerco ruolo tramite id
    public RuoloUtente ruoloPerId(UUID idRuolo) {
        return ruoloUtenteRepository.findById(idRuolo)
                .orElseThrow(() -> new NotFoundException("L'utente con id " + idRuolo + " non è assegnato a quel ruolo"));
    }

    public RuoloUtente findByNomeRuolo(String nomeRuolo) {
        return ruoloUtenteRepository.findByNomeRuolo(nomeRuolo).orElseThrow(() -> new NotFoundException("Ruolo con nome '" + nomeRuolo + "' non trovato"));
    }

}

