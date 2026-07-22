package team6.BW_5.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Fattura;
import team6.BW_5.entities.StatoFattura;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.exceptions.ForbiddenException;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.FatturaRepository;
import team6.BW_5.requestDTO.FatturaDTO;
import team6.BW_5.requestDTO.FatturaPatchDTO;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FatturaService {
    //ATTRIBUTI
    private final FatturaRepository fatturaRepository;
    private final StatoFatturaService statoFatturaService;
    private final ClienteService clienteService;


    public FatturaService(FatturaRepository fatturaRepository, StatoFatturaService statoFatturaService, ClienteService clienteService) {
        this.fatturaRepository = fatturaRepository;
        this.statoFatturaService = statoFatturaService;
        this.clienteService = clienteService;
    }

    //METODI

    //SALVA FATTURA
    @Transactional
    public Fattura saveFattura(FatturaDTO payload) {

        //1. trovo il cliente
        Cliente cliente = clienteService.findById(payload.idCliente());

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
    public Page<Fattura> findAll(int page, int size, String sortBy, Sort.Direction direction) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return fatturaRepository.findAll(pageable);
    }

    //FINDBYID
    public Fattura findById(UUID id) {
        Fattura fatturaTrovata = fatturaRepository.findById(id).orElseThrow(() -> new NotFoundException("la fattura con id " + id + " non è stata trovata"));
        return fatturaTrovata;
    }

    //UPDATE -> POST
    public Fattura updateFattura(UUID id, FatturaDTO payload) {
        Fattura fatturaTrovata = findById(id);

//SE FATTURA TROVATA, GET CLIENTE, SE NON E' ACTIVE SPARA ECCEZIONE

        if (!fatturaTrovata.getCliente().isAttivo())
            throw new ForbiddenException("Impossibile modificare la fattura! Il cliente con id " + fatturaTrovata.getCliente().getIdCliente() + " non è attivo");

        fatturaTrovata.setData(payload.data());
        fatturaTrovata.setImporto(payload.importo());

        Cliente clienteTrovato = clienteService.findById(payload.idCliente());
        fatturaTrovata.setCliente(clienteTrovato);

        return fatturaRepository.save(fatturaTrovata);

    }

    //UPDATE -> PATCH
    public Fattura patchFattura(UUID id, FatturaPatchDTO payload) {
        Fattura fatturaTrovata = findById(id);

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

            Cliente clienteTrovato = clienteService.findById(payload.idCliente());
            fatturaTrovata.setCliente(clienteTrovato);
        }

        return fatturaRepository.save(fatturaTrovata);
    }

    //DELETE
    public void deleteFattura(UUID id) {
        Fattura fatturaTrovata = findById(id);
        fatturaRepository.delete(fatturaTrovata);
    }


    //UPDATE STATO FATTURA
    public Fattura updateStatoFattura(UUID fatturaId, String nuovoStato) {

        //1. recupero la fattura
        Fattura fatturaTrovata = findById(fatturaId);

        String statoAttuale = fatturaTrovata.getStato().getStato();
        String nuovoStatoUpper = nuovoStato.toUpperCase();

        //2. regole di transizione
        if ("PAGATA".equalsIgnoreCase(statoAttuale) && !"PAGATA".equalsIgnoreCase(nuovoStatoUpper)) {
            throw new BadRequestException("Una fattura già PAGATA non può cambiare stato!");
        }

        if ("ANNULLATA".equalsIgnoreCase(statoAttuale)) {
            throw new BadRequestException("Una fattura ANNULLATA non può più cambiare stato!");
        }

        // 3. Chiediamo a StatoFatturaService di trovarci o crearci lo stato valido
        StatoFattura nuovoStatoEntity = statoFatturaService.findByStatoOrCreate(nuovoStatoUpper);

        // 4. Assegniamo e salviamo
        fatturaTrovata.setStato(nuovoStatoEntity);
        return fatturaRepository.save(fatturaTrovata);

    }

}


