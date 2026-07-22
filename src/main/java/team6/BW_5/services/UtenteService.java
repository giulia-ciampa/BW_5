package team6.BW_5.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.UtenteRepository;
import team6.BW_5.requestDTO.UtenteRequestDTO;

import java.util.UUID;

@Service
public class UtenteService {
    private final PasswordEncoder bcrypt;
    private UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder bcrypt) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = bcrypt;
    }

    //metodo per tornare lista di utenti con paginazione inclusa da usare nel getmapping del controller
    public Page<Utente> findAll(Pageable pageable) {
        return utenteRepository.findAll(pageable);
    }

    // findById
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
        return utenteRepository.save(new Utente(body.username(), body.email(), bcrypt.encode(body.password()), body.nome(), body.cognome(), true));
    }

    //metodo per eliminare utente byId
    public void utenteEliminato(UUID id) {
        Utente utente = findById(id);
        utenteRepository.delete(utente);
    }

    //metodo per aggiornare utente
    public Utente utenteAggiornato(UUID id, Utente utenteModificato) {
        Utente utenteEsistente = findById(id);

        // setto i dati utente
        utenteEsistente.setNome(utenteModificato.getNome());
        utenteEsistente.setCognome(utenteModificato.getCognome());
        utenteEsistente.setEmail(utenteModificato.getEmail());
        utenteEsistente.setUsername(utenteModificato.getUsername());
        utenteEsistente.setAvatar(utenteModificato.getAvatar());
        utenteEsistente.setPassword(utenteModificato.getPassword());

        return utenteRepository.save(utenteEsistente);
    }

    //delete per utente tramite id
    public void eliminaUtente(UUID id) {
        Utente utenteDaEliminare = utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Utente non trovato" +
                "con id" + " " + id));
        utenteRepository.delete(utenteDaEliminare);
    }

    //findByEmail
    public Utente findByEmail(String email) {
        Utente utenteTrovato = utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("l'utente con l'email " + email + " non è stato trovato"));
        return utenteTrovato;
    }

}


