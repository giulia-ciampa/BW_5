package team6.BW_5.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.services.RuoloUtenteService;

@Component
public class RuoliUtenteRunner implements CommandLineRunner {

    private final RuoloUtenteService ruoloUtenteService;

    public RuoliUtenteRunner(RuoloUtenteService ruoloUtenteService) {
        this.ruoloUtenteService = ruoloUtenteService;
    }


    @Override
    public void run(String... args) throws Exception {
        creaRuoliDefault();
    }

    public void creaRuoliDefault() {

        try {
            ruoloUtenteService.findByNomeRuolo("ADMIN");
        } catch (NotFoundException ex) {
            ruoloUtenteService.creaRuolo("ADMIN");
        }
        try {
            ruoloUtenteService.findByNomeRuolo("USER");
        } catch (NotFoundException ex) {
            ruoloUtenteService.creaRuolo("USER");
        }

    }

}
