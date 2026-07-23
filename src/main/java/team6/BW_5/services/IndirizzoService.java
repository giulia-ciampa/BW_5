package team6.BW_5.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.repositories.IndirizzoRepository;
import team6.BW_5.requestDTO.IndirizzoDTO;

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
                    .findByViaIgnoreCaseAndCivicoIgnoreCaseAndCapIgnoreCaseAndComune(via, civico, cap, comune)
                    .orElseGet(() -> save(new Indirizzo(via, civico, null, cap, comune)));
        }

        return indirizzoRepository
                .findByViaIgnoreCaseAndCivicoIgnoreCaseAndLocalitaIgnoreCaseAndCapIgnoreCaseAndComune(
                        via, civico, localita, cap, comune)
                .orElseGet(() -> save(new Indirizzo(via, civico, localita, cap, comune)));
    }

    public Indirizzo save(IndirizzoDTO body) {
        Comune comune = comuneService.findByDenominazioneAndProvincia(body.denominazioneComune(), body.siglaProvincia());
        return save(new Indirizzo(body.via(), body.civico(), body.localita(), body.cap(), comune));
    }

    private Indirizzo save(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }

    public Page<Indirizzo> findAll(int page, int size, String sortBy, Sort.Direction direction) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return indirizzoRepository.findAll(pageable);
    }
}
