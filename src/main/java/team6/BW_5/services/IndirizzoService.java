package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.repositories.IndirizzoRepository;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;


    public IndirizzoService(IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
    }


    public Indirizzo findByViaCivicoLocalitaOptionalAndComune(String via, String civico, String localita, String cap, Comune comune) {
        return indirizzoRepository.findByViaCivicoLocalitaOptionalAndComune(via, civico, localita, cap, comune).orElseGet(() -> indirizzoRepository.save(new Indirizzo(via, civico, localita, cap, comune)));
    }
}
