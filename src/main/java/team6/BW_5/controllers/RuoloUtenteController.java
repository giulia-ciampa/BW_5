package team6.BW_5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.services.RuoloUtenteService;

import java.util.List;

@RestController
@RequestMapping("/ruoli")
public class RuoloUtenteController {

    @Autowired
    private RuoloUtenteService ruoloUtenteService;

    // crea ruolo
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RuoloUtente creaRuolo(@RequestBody @Validated RuoloUtente nuovoRuolo, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        } return ruoloUtenteService.creaRuolo(nuovoRuolo.getNomeRuolo());

    }

    // lista ruoli
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<RuoloUtente> listaRuoli() {
        return ruoloUtenteService.findAllRuoli();
    }
}