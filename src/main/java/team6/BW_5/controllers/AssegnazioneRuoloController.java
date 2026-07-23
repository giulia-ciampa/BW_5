package team6.BW_5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.AssegnazioneRuolo;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.UnauthorizedException;
import team6.BW_5.services.AssegnazioneRuoloService;
import team6.BW_5.services.RuoloUtenteService;
import team6.BW_5.services.UtenteService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/assegnazioni")
public class AssegnazioneRuoloController {

    private final AssegnazioneRuoloService assegnazioneRuoloService;

    private final UtenteService utenteService;


    private final RuoloUtenteService ruoloUtenteService;

    public AssegnazioneRuoloController(AssegnazioneRuoloService assegnazioneRuoloService, UtenteService utenteService, RuoloUtenteService ruoloUtenteService) {
        this.assegnazioneRuoloService = assegnazioneRuoloService;
        this.utenteService = utenteService;
        this.ruoloUtenteService = ruoloUtenteService;
    }


    // assegno un ruolo a un utente
    @PostMapping("/utente/{idUtente}/ruolo/{idRuolo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public AssegnazioneRuolo assegnaRuolo(@PathVariable UUID idUtente, @PathVariable UUID idRuolo) {
        Utente utente = utenteService.findById(idUtente);
        RuoloUtente ruolo = ruoloUtenteService.ruoloPerId(idRuolo);
        return assegnazioneRuoloService.assegnaRuolo(utente, ruolo);
    }
    // revoca di ruolo tramite id
    @PatchMapping("/utente/{idUtente}/ruolo/{idRuolo}/revoca")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AssegnazioneRuolo revocaRuolo(@PathVariable UUID idUtente, @PathVariable UUID idRuolo) {
        Utente utente = utenteService.findById(idUtente);
        RuoloUtente ruolo = ruoloUtenteService.ruoloPerId(idRuolo);
        return assegnazioneRuoloService.revocaRuolo(utente, ruolo);
    }

    //storico dei ruoli assegnati ad un utente
    @GetMapping("/utente/{idUtente}/attivi")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<RuoloUtente> getRuoliAttiviPerUtente(@PathVariable UUID idUtente, @AuthenticationPrincipal Utente utenteLoggato) {

        // se utente è ADMIN oppure sta cercando i propri ruoli
        boolean isAdmin = utenteLoggato.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        boolean isStessoUtente = utenteLoggato.getUtenteId().equals(idUtente);

        if (!isAdmin && !isStessoUtente) {
            throw new UnauthorizedException("Non puoi visualizzare i ruoli di un altro utente!");
        }

        Utente utente = utenteService.findById(idUtente);
        return assegnazioneRuoloService.trovaRuoliAttiviPerUtente(utente);
    }

}
