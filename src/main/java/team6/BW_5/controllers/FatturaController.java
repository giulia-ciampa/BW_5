package team6.BW_5.controllers;

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
import team6.BW_5.responseDTO.FatturaCreatedDTO;
import team6.BW_5.services.FatturaService;

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
    public FatturaCreatedDTO saveFattura(@RequestBody @Validated FatturaDTO payload,
                                         BindingResult validationResult,
                                         @AuthenticationPrincipal Utente utenteCorrente
    ) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        Fattura fatturaSalvata = fatturaService.saveFattura(payload);
        return new FatturaCreatedDTO(fatturaSalvata.getId());
    }

    //VISUALIZZA TUTTE LE FATTURE
    @GetMapping
    public Page<Fattura> getAllFatture(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return fatturaService.findAll(page, size, sortBy, direction);
    }
}
