package team6.BW_5.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team6.BW_5.entities.Utente;
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UtenteResponseDTO update(@PathVariable UUID id, @RequestBody UtenteRequestDTO utenteRequestDTO) {
        Utente utenteAggiornato = new Utente(
                utenteRequestDTO.username(),
                utenteRequestDTO.email(),
                utenteRequestDTO.password(),
                utenteRequestDTO.nome(),
                utenteRequestDTO.cognome(),
                true
        );
        Utente utenteModificato = utenteService.utenteAggiornato(id, utenteAggiornato);
        return new UtenteResponseDTO(
                utenteModificato.getUtenteId(),
                utenteModificato.getUsername(),
                utenteModificato.getEmail(),
                utenteModificato.getNome(),
                utenteModificato.getCognome()
        );
    }

    //aggiornamento parziale dto utente
    @PatchMapping("{id}")
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
