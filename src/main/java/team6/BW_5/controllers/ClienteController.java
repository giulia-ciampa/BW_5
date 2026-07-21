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

import java.util.UUID;

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


    @GetMapping("/{clienteId}")
    public Cliente findById(@PathVariable UUID clienteId) {
        return clienteService.findById(clienteId);
    }

    @GetMapping("/me")
    public Page<Cliente> findOwnClienti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "dataInserimento") String sortBy, @RequestParam(defaultValue = "DESC") Sort.Direction direction, @AuthenticationPrincipal Utente utente) {
        return clienteService.findOwnClienti(page, size, sortBy, direction, utente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteCreatedDTO createCliente(@RequestBody @Validated ClienteDTO body, BindingResult validationResult, @AuthenticationPrincipal Utente utente) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        System.out.println(">>> UTENTE AUTENTICATO RICEVUTO: " + utente);
        System.out.println(">>> DTO RICEVUTO: ");
        Cliente saved = clienteService.createCliente(body, utente);
        return new ClienteCreatedDTO(saved.getIdCliente());
    }

    @PutMapping("/{clienteId}")
    public Cliente updateCliente(@RequestBody @Validated ClienteDTO body, BindingResult validationResult, @AuthenticationPrincipal Utente utente, @PathVariable UUID clienteId) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return clienteService.updateCliente(body, utente, clienteId);
    }

    @DeleteMapping("/me/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnCliente(@AuthenticationPrincipal Utente utenteAutenticato, @PathVariable UUID clienteId) {
        clienteService.deleteOwnCliente(utenteAutenticato, clienteId);
    }

}
