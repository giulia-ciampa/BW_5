package team6.BW_5.services;

import team6.BW_5.entities.StatoFattura;
import team6.BW_5.repositories.StatoFatturaRepository;

public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;

    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    public StatoFattura trovaOAlimentaStato(String stato) {
        return statoFatturaRepository.findByStato(stato).orElseGet(() -> {
            StatoFattura nuovoStato = new StatoFattura();
            nuovoStato.setStato(stato);
            return statoFatturaRepository.save(nuovoStato);
        });
    }
}
