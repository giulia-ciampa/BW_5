package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.repositories.IndirizzoRepository;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;
    private final ComuneService comuneService;


    public IndirizzoService(IndirizzoRepository indirizzoRepository, ComuneService comuneService) {
        this.indirizzoRepository = indirizzoRepository;
        this.comuneService = comuneService;
    }


    public Indirizzo findByViaCivicoLocalitaOptionalAndComune(
            String via,
            String civico,
            String localita,
            String cap,
            String denominazioneComune,
            String siglaProvincia

    ) {
        Comune comune = comuneService.findByDenominazioneAndProvincia(denominazioneComune, siglaProvincia);
        if (localita == null) {
            return indirizzoRepository
                    .findByViaIgnoreCaseAndCivicoIgnoreCaseAndCapIgnoreCaseAndComune(
                            via, civico, cap, comune
                    ).orElseGet(() -> indirizzoRepository.save(new Indirizzo(via, civico, null, cap, comune)));
        }

        return indirizzoRepository
                .findByViaIgnoreCaseAndCivicoIgnoreCaseAndLocalitaIgnoreCaseAndCapIgnoreCaseAndComune(
                        via, civico, localita, cap, comune
                ).orElseGet(() -> indirizzoRepository.save(new Indirizzo(via, civico, localita, cap, comune)));
    }
}
