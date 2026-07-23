package team6.BW_5.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team6.BW_5.services.StatoFatturaService;

@Component
public class StatoFatturaRunner implements CommandLineRunner {

    private final StatoFatturaService statoFatturaService;

    public StatoFatturaRunner(StatoFatturaService statoFatturaService) {
        this.statoFatturaService = statoFatturaService;

    }


    @Override
    public void run(String... args) throws Exception {
        for (String stato : statoFatturaService.getStatiValidi()) {
            statoFatturaService.creaStatoSeNonEsiste(stato);
        }
    }
}

