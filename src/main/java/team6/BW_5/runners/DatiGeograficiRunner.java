package team6.BW_5.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team6.BW_5.services.ComuneService;
import team6.BW_5.services.ProvinciaService;

@Component
public class DatiGeograficiRunner implements CommandLineRunner {

    public final ComuneService comuneService;
    public final ProvinciaService provinciaService;

    public DatiGeograficiRunner(ComuneService comuneService, ProvinciaService provinciaService) {
        this.comuneService = comuneService;
        this.provinciaService = provinciaService;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
