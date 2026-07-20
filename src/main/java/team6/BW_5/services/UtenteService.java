package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.entities.Utente;
import team6.BW_5.repositories.UtenteRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UtenteService {
    private UtenteRepository utenteRepository;

    //metodo per tornare lista di utenti
    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    // findById
    public Utente findById(UUID id) {
        return utenteRepository.findById(id).orElseThrow(()-> new RuntimeException("L'utente con id" + " " + id + " non è stato trovato"));
    }

    // salvo utente, ma prima controllo se email e username inseriti non siano gia nel db
    public Utente utenteSalvato(Utente utente) {
        if(utenteRepository.existsByEmail(utente.getEmail())) {
            throw new RuntimeException("L'email inserita è gia in uso!");
        }
            if(utenteRepository.existsByUsername(utente.getUsername())){
                throw new RuntimeException("L'username inserito è gia nei nostri database!");
            }
    return utenteRepository.save(utente);
}

//metodo per eliminare utente byId
    public void utenteEliminato(UUID id) {
        Utente utente = findById(id);
        utenteRepository.delete(utente);
    }



}
