package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.StatoFattura;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.repositories.StatoFatturaRepository;

import java.util.List;


@Service
public class StatoFatturaService {

    private static final List<String> statiValidi = List.of("EMESSA", "DA_PAGARE", "PAGATA", "ANNULLATA");
    private final StatoFatturaRepository statoFatturaRepository;


    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;

    }

    //CERCA O SALVA UNO STATO
    public StatoFattura findByStatoOrCreate(String nuovoStato) {
        String statoUpper = nuovoStato.toUpperCase();

        //1. controllo validità stringa
        if (!statiValidi.contains(statoUpper)) {
            throw new BadRequestException("Stato non valido! Gli stati ammessi sono: " + statiValidi);
        }

        // 2. Cerca nel DB, se non c'è lo crea al volo (solo se appartiene a statiValidi)
        return statoFatturaRepository.findByStato(nuovoStato)
                .orElseGet(() -> {
                    StatoFattura ns = new StatoFattura();
                    ns.setStato(nuovoStato);
                    return statoFatturaRepository.save(ns);
                });
    }

    //SALVA FATTURA CON STATO "EMESSA"
    public StatoFattura salvaEmissioneFattura() {
        return findByStatoOrCreate("EMESSA");
    }

}

