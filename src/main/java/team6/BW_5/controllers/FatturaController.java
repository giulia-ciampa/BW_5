package team6.BW_5.controllers;

import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import team6.BW_5.requestDTO.StatoFatturaDTO;
import team6.BW_5.responseDTO.FatturaCreatedDTO;
import team6.BW_5.responseDTO.StatoFatturaUpdatedDTO;
import team6.BW_5.services.FatturaService;
import team6.BW_5.services.StatoFatturaService;

import java.util.UUID;

@RestController
@RequestMapping("/fatture")
public class FatturaController {

    private final FatturaService fatturaService;
    private final StatoFatturaService statoFatturaService;

    public FatturaController(FatturaService fatturaService, StatoFatturaService statoFatturaService) {
        this.fatturaService = fatturaService;
        this.statoFatturaService = statoFatturaService;
    }

    //SALVA FATTURA
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FatturaCreatedDTO saveFattura(@RequestBody @Validated FatturaDTO payload,
                                         BindingResult validationResult,
                                         @AuthenticationPrincipal Utente utenteCorrente
    ) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        Fattura fatturaSalvata = fatturaService.saveFattura(payload);
        return new FatturaCreatedDTO(fatturaSalvata.getFatturaId());
    }

    //VISUALIZZA TUTTE LE FATTURE
    @GetMapping
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
    public Fattura findById(@PathVariable UUID idFattura) {
        Fattura fatturaTrovata = fatturaService.findById(idFattura);
        return fatturaTrovata;
    }


    // UPDATE FATTURA
    @PutMapping("/{idFattura}")
    public Fattura updateFattura(@PathVariable UUID idFattura,
                                 @RequestBody @Validated FatturaDTO body,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        return fatturaService.updateFattura(idFattura, body);
    }


    //UPDATE FATTURA PATCH
    @PatchMapping("/{idFattura}")
    public Fattura patchFattura(@PathVariable UUID idFattura,
                                @RequestBody @Validated FatturaPatchDTO payload,
                                BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        return fatturaService.patchFattura(idFattura, payload);
    }

    //DELETE
    @DeleteMapping("/{idFattura}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFattura(@PathVariable UUID idFattura) {
        fatturaService.deleteFattura(idFattura);
    }


    //UPDATE STATO FATTURA
    @PatchMapping("/{idFattura}/stato")
    public StatoFatturaUpdatedDTO updateStatoFattura(
            @PathVariable UUID idFattura,
            @RequestBody @Validated StatoFatturaDTO payload) {
        Fattura fatturaAggiornata = fatturaService.updateStatoFattura(idFattura, payload.nuovoStato());

        return new StatoFatturaUpdatedDTO(
                fatturaAggiornata.getFatturaId(),
                fatturaAggiornata.getStato().getStato(),
                "Stato fattura aggiornato con successo!"
        );
    }

    //TROVA FATTURE CON QUELLO STATO
    @GetMapping("/stato/{nomeStato}")
    public Page<Fattura> getFattureByStato(
            @PathVariable String nomeStato,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {

        return fatturaService.findByStato(nomeStato, page, size, sortBy);
    }

}


