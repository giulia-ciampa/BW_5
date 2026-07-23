package team6.BW_5.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Fattura;
import team6.BW_5.entities.StatoFattura;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.exceptions.ForbiddenException;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.FatturaRepository;
import team6.BW_5.requestDTO.FatturaDTO;
import team6.BW_5.requestDTO.FatturaFilterDTO;
import team6.BW_5.requestDTO.FatturaPatchDTO;
import team6.BW_5.specifications.FatturaSpecifications;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Service
public class FatturaService {
    //ATTRIBUTI
    private final FatturaRepository fatturaRepository;
    private final StatoFatturaService statoFatturaService;
    private final ClienteService clienteService;
    private FatturaSpecifications fatturaSpecifications;


    public FatturaService(FatturaRepository fatturaRepository,
                          StatoFatturaService statoFatturaService,
                          ClienteService clienteService,
                          FatturaSpecifications fatturaSpecifications) {
        this.fatturaRepository = fatturaRepository;
        this.statoFatturaService = statoFatturaService;
        this.clienteService = clienteService;
        this.fatturaSpecifications = fatturaSpecifications;
    }

    //METODI

    //SALVA FATTURA
    @Transactional
    public Fattura saveFattura(FatturaDTO payload, Utente utenteCorrente) {

        //1. trovo il cliente
        Cliente cliente = clienteService.findById(payload.idCliente());

        // Verifico se l'utente è il proprietario DEL cliente
        boolean isOwner = cliente.getUtente().getUtenteId().equals(utenteCorrente.getUtenteId());

        // Verifico se l'utente ha l'authority ADMIN (o ROLE_ADMIN)
        boolean isAdmin = utenteCorrente.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        // Se NON è il proprietario E NON è un admin, blocco la richiesta
        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("Non hai i permessi per creare una fattura per questo cliente");
        }

        if (!cliente.isAttivo())
            throw new ForbiddenException("impossibile salvare la fattura, il cliente con id " + cliente.getIdCliente() + " non è attivo");

        //2. stato iniziale
        StatoFattura statoIniziale = statoFatturaService.salvaEmissioneFattura();

        //3. nuova fattura usando il costruttore
        Fattura nuovaFattura = new Fattura(payload.data(), payload.importo(), statoIniziale, cliente);

        //4. calcolo numero progressivo annuale

        //estrazione anno corrente
        int anno = payload.data().getYear();

        // oggetto che indica il primo giorno dell'anno
        LocalDate inizioAnno = LocalDate.of(anno, 1, 1);

        //oggetto che indica l'ultimo giorno dell'anno
        LocalDate fineAnno = LocalDate.of(anno, 12, 31);

        //query
        long ultimoNumero = fatturaRepository.findFirstByDataBetweenOrderByNumeroDesc(inizioAnno, fineAnno).map(fattura -> fattura.getNumero()).orElse(0L);

        nuovaFattura.setNumero(ultimoNumero + 1);

        return fatturaRepository.save(nuovaFattura);
    }

    //FIND ALL
    public Page<Fattura> findAll(int page, int size, String sortBy, Sort.Direction direction, FatturaFilterDTO filters) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Fattura> spec = fatturaSpecifications.specificationFatturaBuilder(filters);

        if (spec == null) {
            return fatturaRepository.findAll(pageable);
        }

        return fatturaRepository.findAll(spec, pageable);
    }


    //FINDBYID
    public Fattura findById(UUID id, Utente utenteCorrente) {
        Fattura fatturaTrovata = fatturaRepository.findById(id).orElseThrow(() -> new NotFoundException("la fattura con id " + id + " non è stata trovata"));

        boolean isOwner = fatturaTrovata.getCliente() != null
                && fatturaTrovata.getCliente().getUtente() != null
                && fatturaTrovata.getCliente().getUtente().getUtenteId().equals(utenteCorrente.getUtenteId());

        if (isOwner || isAdmin(utenteCorrente)) {
            return fatturaTrovata;
        } else {
            throw new ForbiddenException("Non hai i permessi per visualizzare questa fattura");
        }


    }

    //UPDATE -> PUT
    public Fattura updateFattura(UUID id, Utente utenteCorrente, FatturaDTO payload) {

        // Verifico l'authority Admin
        boolean isAdmin = utenteCorrente.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        Fattura fatturaTrovata = findById(id, utenteCorrente);


        if (!fatturaTrovata.getCliente().isAttivo())
            throw new ForbiddenException("Impossibile modificare la fattura! Il cliente con id " + fatturaTrovata.getCliente().getIdCliente() + " non è attivo");

        //Recupero il nuovo cliente dal payload
        Cliente nuovoCliente = clienteService.findById(payload.idCliente());

        boolean isOwnerOfNewClient = nuovoCliente.getUtente().getUtenteId().equals(utenteCorrente.getUtenteId());

        if (!isOwnerOfNewClient && !isAdmin) {
            throw new ForbiddenException("Non puoi assegnare questa fattura a un cliente non tuo!");
        }

        if (!nuovoCliente.isAttivo()) {
            throw new ForbiddenException("Impossibile riassegnare la fattura a un cliente non attivo!");
        }

        fatturaTrovata.setData(payload.data());
        fatturaTrovata.setImporto(payload.importo());
        fatturaTrovata.setCliente(nuovoCliente);

        return fatturaRepository.save(fatturaTrovata);

    }

    //UPDATE -> PATCH
    public Fattura patchFattura(UUID id, Utente utenteCorrente, FatturaPatchDTO payload) {

        boolean isAdmin = utenteCorrente.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        Fattura fatturaTrovata = findById(id, utenteCorrente);

        //ECCEZIONE CLIENTE NON ATTIVO
        if (!fatturaTrovata.getCliente().isAttivo())
            throw new ForbiddenException("Impossibile modificare la fattura! Il cliente con id " + fatturaTrovata.getCliente().getIdCliente() + " non è attivo");

        if (payload.data() != null) {
            fatturaTrovata.setData(payload.data());
        }


        if (payload.importo() != null) {
            fatturaTrovata.setImporto(payload.importo());
        }

        if (payload.idCliente() != null) {

            // 1. Recupero il NUOVO cliente dal payload
            Cliente nuovoCliente = clienteService.findById(payload.idCliente());

            // 2. Controllo se il NUOVO cliente appartiene all'utente loggato
            boolean isOwnerOfNewClient = nuovoCliente.getUtente().getUtenteId().equals(utenteCorrente.getUtenteId());

            if (!isOwnerOfNewClient && !isAdmin) {
                throw new ForbiddenException("Non puoi assegnare questa fattura a un cliente non tuo!");
            }

            // 3. Controllo se il nuovo cliente è attivo
            if (!nuovoCliente.isAttivo()) {
                throw new ForbiddenException("Impossibile riassegnare la fattura a un cliente non attivo!");
            }

            fatturaTrovata.setCliente(nuovoCliente);
        }

        return fatturaRepository.save(fatturaTrovata);
    }

    //DELETE
    public void deleteFattura(UUID id, Utente utenteCorrente) {
        Fattura fatturaTrovata = findById(id, utenteCorrente);
        fatturaRepository.delete(fatturaTrovata);
    }


    //UPDATE STATO FATTURA
    public Fattura updateStatoFattura(UUID fatturaId, String nuovoStato, Utente utenteCorrente) {

        //1. recupero la fattura
        Fattura fatturaTrovata = findById(fatturaId, utenteCorrente);

        //2. VERIFICA CLIENTE ATTIVO
        if (!fatturaTrovata.getCliente().isAttivo()) {
            throw new ForbiddenException("Impossibile modificare lo stato della fattura! Il cliente con id " + fatturaTrovata.getCliente().getIdCliente() + " non è attivo");
        }

        String statoAttuale = fatturaTrovata.getStato().getStato();
        String nuovoStatoUpper = nuovoStato.toUpperCase();

        //3. regole di transizione
        if ("PAGATA".equalsIgnoreCase(statoAttuale) && !"PAGATA".equalsIgnoreCase(nuovoStatoUpper)) {
            throw new BadRequestException("Una fattura già PAGATA non può cambiare stato!");
        }

        if ("ANNULLATA".equalsIgnoreCase(statoAttuale)) {
            throw new BadRequestException("Una fattura ANNULLATA non può più cambiare stato!");
        }

        // 4. Chiedo a StatoFatturaService di trovarci lo stato valido
        StatoFattura nuovoStatoEntity = statoFatturaService.findByStato(nuovoStatoUpper);

        // 5. Assegniamo e salviamo
        fatturaTrovata.setStato(nuovoStatoEntity);
        return fatturaRepository.save(fatturaTrovata);

    }

    //TROVA FATTURE CON QUELLO STATO
    public Page<Fattura> findByStato(String nomeStato, int page, int size, String sortBy) {
        StatoFattura stato = statoFatturaService.findByStato(nomeStato);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        return fatturaRepository.findByStato(stato, pageable);

    }

    //ISADMIN
    private boolean isAdmin(Utente utente) {
        return utente != null && utente.getAuthorities() != null && utente.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ADMIN"));
    }

}




