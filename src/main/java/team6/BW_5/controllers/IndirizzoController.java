package team6.BW_5.controllers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.requestDTO.IndirizzoDTO;
import team6.BW_5.responseDTO.IndirizzoCreatedDTO;
import team6.BW_5.services.IndirizzoService;

@RestController
@RequestMapping("/indirizzi")
public class IndirizzoController {

    private final IndirizzoService indirizzoService;


    public IndirizzoController(IndirizzoService indirizzoService) {
        this.indirizzoService = indirizzoService;
    }

    @GetMapping
    public Page<Indirizzo> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cap") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return indirizzoService.findAll(page, size, sortBy, direction);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public IndirizzoCreatedDTO save(@RequestBody @Validated IndirizzoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors())
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        Indirizzo indirizzo = indirizzoService.save(body);
        return new IndirizzoCreatedDTO(indirizzo.getIndirizzoId());
    }
}
