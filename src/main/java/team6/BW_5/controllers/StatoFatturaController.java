package team6.BW_5.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team6.BW_5.entities.StatoFattura;
import team6.BW_5.services.StatoFatturaService;

import java.util.List;

@RestController
@RequestMapping("/stati-fattura")
public class StatoFatturaController {
    private final StatoFatturaService statoFatturaService;

    public StatoFatturaController(StatoFatturaService statoFatturaService) {
        this.statoFatturaService = statoFatturaService;
    }

    //VISUALIZZA STATI FATTURA
    @GetMapping
    public List<StatoFattura> getAllStatiFattura() {
        return statoFatturaService.findAll();
    }
}
