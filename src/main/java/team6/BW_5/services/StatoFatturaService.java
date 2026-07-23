package team6.BW_5.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.StatoFattura;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.StatoFatturaRepository;

import java.util.List;


@Service
public class StatoFatturaService {


    @Getter
    private final List<String> statiValidi = List.of("EMESSA", "DA PAGARE", "PAGATA", "ANNULLATA");
    private final StatoFatturaRepository statoFatturaRepository;


    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;

    }

    //CERCA UNO STATO A DB
    public StatoFattura findByStato(String nuovoStato) {
        String statoUpper = nuovoStato.toUpperCase();

        //1. controllo validità stringa
        if (!statiValidi.contains(statoUpper)) {
            throw new BadRequestException("Stato non valido! Gli stati ammessi sono: " + statiValidi);
        }

        // 2. Cerca nel DB, se non c'è lancia eccezione
        return statoFatturaRepository.findByStato(statoUpper).orElseThrow(() -> new NotFoundException("lo stato " + statoUpper + " non è stato trovato"));

    }

    // METODO PER IL RUNNER: CERCA O CREA LO STATO SE MANCA
    public void creaStatoSeNonEsiste(String stato) {
        String statoUpper = stato.toUpperCase();
        if (statoFatturaRepository.findByStato(statoUpper).isEmpty()) {
            StatoFattura nuovoStato = new StatoFattura();
            nuovoStato.setStato(statoUpper);
            statoFatturaRepository.save(nuovoStato);
        }
    }

    //SALVA FATTURA CON STATO "EMESSA"
    public StatoFattura salvaEmissioneFattura() {
        return findByStato("EMESSA");
    }

    //VISUALIZZA TUTTI GLI STATI
    public List<StatoFattura> findAll() {
        return statoFatturaRepository.findAll();
    }

}

