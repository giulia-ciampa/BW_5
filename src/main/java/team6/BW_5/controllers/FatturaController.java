package team6.BW_5.controllers;

import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.Fattura;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.requestDTO.FatturaDTO;
import team6.BW_5.requestDTO.FatturaFilterDTO;
import team6.BW_5.requestDTO.FatturaPatchDTO;
import team6.BW_5.responseDTO.FatturaCreatedDTO;
import team6.BW_5.services.FatturaService;

import java.util.UUID;

@RestController
@RequestMapping("/fatture")
public class FatturaController {

    private final FatturaService fatturaService;


    public FatturaController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
        
    }

    //SALVA FATTURA
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public FatturaCreatedDTO saveFattura(@RequestBody @Validated FatturaDTO payload,
                                         BindingResult validationResult,
                                         @AuthenticationPrincipal Utente utenteCorrente
    ) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        Fattura fatturaSalvata = fatturaService.saveFattura(payload, utenteCorrente);
        return new FatturaCreatedDTO(fatturaSalvata.getFatturaId());
    }

    //VISUALIZZA TUTTE LE FATTURE
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Fattura> getAllFatture(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @Valid @ModelAttribute FatturaFilterDTO filters
    ) {
        return fatturaService.findAll(page, size, sortBy, direction, filters);
    }

    //TROVA LA FATTURA PER ID
    @GetMapping("/{idFattura}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Fattura findById(@PathVariable UUID idFattura, @AuthenticationPrincipal Utente utenteCorrente) {
        Fattura fatturaTrovata = fatturaService.findById(idFattura, utenteCorrente);
        return fatturaTrovata;
    }


    // UPDATE FATTURA
    @PutMapping("/{idFattura}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Fattura updateFattura(@PathVariable UUID idFattura,
                                 @AuthenticationPrincipal Utente utenteCorrente,
                                 @RequestBody @Validated FatturaDTO body,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        return fatturaService.updateFattura(idFattura, utenteCorrente, body);
    }


    //UPDATE FATTURA PATCH
    @PatchMapping("/{idFattura}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Fattura patchFattura(@PathVariable UUID idFattura,
                                @RequestBody @Validated FatturaPatchDTO payload,
                                BindingResult validationResult,
                                @AuthenticationPrincipal Utente utenteCorrente) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        return fatturaService.patchFattura(idFattura, utenteCorrente, payload);
    }

    //DELETE
    @DeleteMapping("/{idFattura}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFattura(@PathVariable UUID idFattura, @AuthenticationPrincipal Utente utenteCorrente) {
        fatturaService.deleteFattura(idFattura, utenteCorrente);
    }


//    //UPDATE STATO FATTURA
//    @PatchMapping("/{idFattura}/stato")
//    public StatoFatturaUpdatedDTO updateStatoFattura(
//            @PathVariable UUID idFattura,
//            @RequestBody @Validated StatoFatturaDTO payload,
//            @AuthenticationPrincipal Utente utenteCorrente) {
//        Fattura fatturaAggiornata = fatturaService.updateStatoFattura(idFattura, payload.nuovoStato(), utenteCorrente);
//
//        return new StatoFatturaUpdatedDTO(
//                fatturaAggiornata.getFatturaId(),
//                fatturaAggiornata.getStato().getStato(),
//                "Stato fattura aggiornato con successo!"
//        );
//    }

    //TROVA FATTURE CON QUELLO STATO
    @GetMapping("/stato/{nomeStato}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Fattura> getFattureByStato(
            @PathVariable String nomeStato,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {

        return fatturaService.findByStato(nomeStato, page, size, sortBy);
    }

}


