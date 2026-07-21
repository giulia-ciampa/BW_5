package team6.BW_5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.services.RuoloUtenteService;

import java.util.List;

@RestController
@RequestMapping("/ruoli")
public class RuoloUtenteController {

    @Autowired
    private RuoloUtenteService ruoloUtenteService;

    // crea ruolo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RuoloUtente creaRuolo(@RequestBody RuoloUtente nuovoRuolo) {
        return ruoloUtenteService.creaRuolo(nuovoRuolo.getNomeRuolo());
    }

    // lista ruoli
    @GetMapping
    public List<RuoloUtente> listaRuoli() {
        return ruoloUtenteService.findAllRuoli();
    }
}