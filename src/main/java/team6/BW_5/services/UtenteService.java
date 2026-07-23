package team6.BW_5.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.UnauthorizedException;
import team6.BW_5.repositories.UtenteRepository;
import team6.BW_5.requestDTO.UtenteRequestDTO;
import team6.BW_5.responseDTO.UtentePatchDTO;

import java.util.Objects;
import java.util.UUID;

@Service
public class UtenteService {
    private final PasswordEncoder bcrypt;
    private final AssegnazioneRuoloService assegnazioneRuoloService;
    private final RuoloUtenteService ruoloUtenteService;
    private UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder bcrypt, AssegnazioneRuoloService assegnazioneRuoloService, RuoloUtenteService ruoloUtenteService) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = bcrypt;
        this.assegnazioneRuoloService = assegnazioneRuoloService;
        this.ruoloUtenteService = ruoloUtenteService;
    }

    //metodo per tornare lista di utenti con paginazione inclusa da usare nel getmapping del controller
    public Page<Utente> findAll(Pageable pageable) {
        return utenteRepository.findAll(pageable);
    }

    // find all per gli utenti attivi
    public Page<Utente> findAllAttivi(Pageable pageable) {
        return utenteRepository.findByIsAttivoTrue(pageable);
    }

    //findById
    public Utente findById(UUID id) {
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id" + " " + id + " non è stato trovato"));
    }

    // salvo utente, ma prima controllo se email e username inseriti non siano gia nel db
    public Utente salvaUtente(UtenteRequestDTO body) {
        if (utenteRepository.existsByEmail(body.email())) {
            throw new RuntimeException("L'email inserita è gia in uso!");
        }
        if (utenteRepository.existsByUsername(body.username())) {
            throw new RuntimeException("L'username inserito è gia nei nostri database!");
        }
        Utente nuovoUtente = utenteRepository.save(new Utente(body.username(), body.email(), bcrypt.encode(body.password()), body.nome(), body.cognome(), true));
        assegnazioneRuoloService.assegnaRuolo(nuovoUtente, ruoloUtenteService.findByNomeRuolo("USER"));

        return nuovoUtente;
    }


    //metodo per aggiornare utente
    public Utente utenteAggiornato(UUID id, UtenteRequestDTO body, Utente utente) {
        Utente utenteEsistente = findById(id);
        if (utenteEsistente.getUtenteId().equals(utente.getUtenteId()) || utente.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ADMIN"))) {
            // setto i dati utente
            utenteEsistente.setNome(body.nome());
            utenteEsistente.setCognome(body.cognome());
            utenteEsistente.setEmail(body.email());
            utenteEsistente.setUsername(body.username());

            return utenteRepository.save(utenteEsistente);
        } else throw new UnauthorizedException("Non sei abilitato ad aggiornare questo utente");

    }

    //soft delete
    public void eliminaUtente(UUID id) {
        Utente utenteDaEliminare = findById(id); // trovo utente o lancio ecc

        utenteDaEliminare.setAttivo(false);   // se non è attivo "lo spengo" (Soft Delete)

        utenteRepository.save(utenteDaEliminare);
    }

    //find by email e se e attiva
    public Utente findByEmail(String email) {
        return utenteRepository.findByEmailAndIsAttivoTrue(email)
                .orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato o non è attivo"));
    }

    public Utente findByUsername(String username) {
        return utenteRepository.findByUsernameAndIsAttivoTrue(username)
                .orElseThrow(() -> new NotFoundException("L'utente con username " + username + " non è stato trovato o non è attivo"));
    }

    //patch per modificare solo parte dell'utente
    public Utente aggiornaParzialmenteUtente(UUID id, UtentePatchDTO patchDTO) {
        Utente utenteEsistente = findById(id);

        if (patchDTO.id() != null) {
            throw new IllegalArgumentException("Non puoi modificare l'ID!");
        }

        if (patchDTO.username() != null) {
            utenteEsistente.setUsername(patchDTO.username());
        }
        if (patchDTO.email() != null) {
            utenteEsistente.setEmail(patchDTO.email());
        }
        if (patchDTO.password() != null) {
            utenteEsistente.setPassword(bcrypt.encode(patchDTO.password()));
        }
        if (patchDTO.nome() != null) {
            utenteEsistente.setNome(patchDTO.nome());
        }
        if (patchDTO.cognome() != null) {
            utenteEsistente.setCognome(patchDTO.cognome());
        }

        return utenteRepository.save(utenteEsistente);
    }

}


