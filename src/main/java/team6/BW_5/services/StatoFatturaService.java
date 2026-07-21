package team6.BW_5.services;

import team6.BW_5.entities.StatoFattura;
import team6.BW_5.repositories.StatoFatturaRepository;

public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;

    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    public StatoFattura salvaEmissioneFattura() {
        return statoFatturaRepository.findByStato("EMESSA")
                .orElseGet(() -> {
                    StatoFattura nuovoStato = new StatoFattura();
                    nuovoStato.setStato("EMESSA");
                    return statoFatturaRepository.save(nuovoStato);
                });
    }
}
