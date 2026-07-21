package team6.BW_5.controllers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.requestDTO.ClienteDTO;
import team6.BW_5.responseDTO.ClienteCreatedDTO;
import team6.BW_5.services.ClienteService;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public Page<Cliente> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "dataInserimento") String sortBy, @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return clienteService.findAll(page, size, sortBy, direction);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteCreatedDTO createCliente(@RequestBody @Validated ClienteDTO body, @AuthenticationPrincipal Utente utente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        Cliente saved = clienteService.createCliente(body, utente);
        return new ClienteCreatedDTO(saved.getId());
    }
}
