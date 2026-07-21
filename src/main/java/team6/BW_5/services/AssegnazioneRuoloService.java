package team6.BW_5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.AssegnazioneRuolo;
import team6.BW_5.entities.RuoloUtente;
import team6.BW_5.entities.Utente;
import team6.BW_5.repositories.AssegnazioneRuoloRepository;
import java.time.LocalDate;
import java.util.List;


@Service
public class AssegnazioneRuoloService {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RuoloUtenteService ruoloUtenteService;

    private final AssegnazioneRuoloRepository assegnazioneRuoloRepository;

    public AssegnazioneRuoloService(AssegnazioneRuoloRepository assegnazioneRuoloRepository) {
        this.assegnazioneRuoloRepository = assegnazioneRuoloRepository;
    }

    // assegno un nuovo ruolo a un utente
    public AssegnazioneRuolo assegnaRuolo(Utente utente, RuoloUtente ruolo) {
        // verifica se l'utente ha già quel ruolo attivo
        boolean giaAssegnato = assegnazioneRuoloRepository
                .findByUtenteAndRuoloAndDataRevocaIsNull(utente, ruolo)
                .isPresent();

        if (giaAssegnato) {
            throw new RuntimeException("L'utente ha già questo ruolo attivo!");
        }

        AssegnazioneRuolo nuovaAssegnazione = new AssegnazioneRuolo(utente, ruolo, LocalDate.now());
        return assegnazioneRuoloRepository.save(nuovaAssegnazione);
    }

    // revoca un ruolo attivo impostando la data attuale localdate.now
    public AssegnazioneRuolo revocaRuolo(Utente utente, RuoloUtente ruolo) {
        AssegnazioneRuolo assegnazione = assegnazioneRuoloRepository
                .findByUtenteAndRuoloAndDataRevocaIsNull(utente, ruolo)
                .orElseThrow(() -> new RuntimeException("Assegnazione attiva non trovata per questo utente e ruolo"));

        assegnazione.setDataRevoca(LocalDate.now());
        return assegnazioneRuoloRepository.save(assegnazione);
    }

    public List<RuoloUtente> trovaRuoliAttiviPerUtente(Utente utente) {
        List<AssegnazioneRuolo> assegnazioniAttive = assegnazioneRuoloRepository.findByUtenteAndDataRevocaIsNull(utente);
        //  lista dei ruoli effettivi dalle assegnazioni attive
        return assegnazioniAttive.stream()
                .map(AssegnazioneRuolo::getRuolo)
                .toList();
    }

}