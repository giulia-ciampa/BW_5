package team6.BW_5.controllers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.requestDTO.UtenteRequestDTO;
import team6.BW_5.responseDTO.UtentePatchDTO;
import team6.BW_5.responseDTO.UtenteResponseDTO;
import team6.BW_5.services.UtenteService;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    //get per leggere la lista degli utenti impaginata

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UtenteRequestDTO> findAll(Pageable pageable) {
        Page<Utente> listaUtenti = utenteService.findAll(pageable);

        //ritorno lista utenti con i requisiti
        return listaUtenti.map(utente -> new UtenteRequestDTO(
                utente.getUsername(),
                utente.getEmail(),
                utente.getPassword(),
                utente.getNome(),
                utente.getCognome()

        ));
    }

    //get per leggere un singolo utente tramite id

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public UtenteResponseDTO findById(@PathVariable UUID id) {
        Utente utente = utenteService.findById(id);
        return new UtenteResponseDTO(
                utente.getUtenteId(),
                utente.getUsername(),
                utente.getEmail(),
                utente.getNome(),
                utente.getCognome()

        );
    }


    //patch per aggiornare un utente esistente nel db tramite id
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    // Todo:
    public UtenteResponseDTO update(@PathVariable UUID id, @RequestBody @Validated UtenteRequestDTO utenteRequestDTO, BindingResult validationResult, @AuthenticationPrincipal Utente utente) {

        if (validationResult.hasErrors())
            throw new ValidationException(validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());

        Utente utenteModificato = utenteService.utenteAggiornato(id, utenteRequestDTO, utente);
        return new UtenteResponseDTO(
                utenteModificato.getUtenteId(),
                utenteModificato.getUsername(),
                utenteModificato.getEmail(),
                utenteModificato.getNome(),
                utenteModificato.getCognome()
        );
    }

    @PatchMapping("/me/avatar")
    public Utente updateOwnProfilePic(@AuthenticationPrincipal Utente utente, @RequestParam("profile_picture") MultipartFile file) {
        return utenteService.updateProfilePic(utente, file);
    }

    //aggiornamento parziale dto utente
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UtenteResponseDTO updatePartial(@PathVariable UUID id, @RequestBody UtentePatchDTO patchDTO) {
        Utente utenteModificato = utenteService.aggiornaParzialmenteUtente(id, patchDTO);
        return new UtenteResponseDTO(
                utenteModificato.getUtenteId(),
                utenteModificato.getUsername(),
                utenteModificato.getEmail(),
                utenteModificato.getNome(),
                utenteModificato.getCognome()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        utenteService.eliminaUtente(id);
    }

}
