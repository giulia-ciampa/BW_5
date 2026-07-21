package team6.BW_5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.AssegnazioneRuolo;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.entities.Utente;
import team6.BW_5.services.AssegnazioneRuoloService;
import team6.BW_5.services.RuoloUtenteService;
import team6.BW_5.services.UtenteService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/assegnazioni")
public class AssegnazioneRuoloController {
    @Autowired
    private AssegnazioneRuoloService assegnazioneRuoloService;
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private RuoloUtenteService ruoloUtenteService;


    // assegno un ruolo a un utente
    @PostMapping("/utente/{idUtente}/ruolo/{idRuolo}")
    @ResponseStatus(HttpStatus.CREATED)
    public AssegnazioneRuolo assegnaRuolo(@PathVariable UUID idUtente, @PathVariable UUID idRuolo) {
        Utente utente = utenteService.findById(idUtente);
        RuoloUtente ruolo = ruoloUtenteService.ruoloPerId(idRuolo);
        return assegnazioneRuoloService.assegnaRuolo(utente, ruolo);
    }
    // revoca di ruolo tramite id
    @PatchMapping("/utente/{idUtente}/ruolo/{idRuolo}/revoca")
    public AssegnazioneRuolo revocaRuolo(@PathVariable UUID idUtente, @PathVariable UUID idRuolo) {
        Utente utente = utenteService.findById(idUtente);
        RuoloUtente ruolo = ruoloUtenteService.ruoloPerId(idRuolo);
        return assegnazioneRuoloService.revocaRuolo(utente, ruolo);
    }

    //storico dei ruoli assegnati ad un utente
    @GetMapping("/utente/{idUtente}/attivi")
    public List<RuoloUtente> getRuoliAttiviPerUtente(@PathVariable UUID idUtente) {
        Utente utente = utenteService.findById(idUtente);
        return assegnazioneRuoloService.trovaRuoliAttiviPerUtente(utente);
    }

}
