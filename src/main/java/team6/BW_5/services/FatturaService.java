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
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.repositories.ClienteRepository;
import team6.BW_5.repositories.FatturaRepository;
import team6.BW_5.repositories.StatoFatturaRepository;
import team6.BW_5.requestDTO.FatturaDTO;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FatturaService {
    //ATTRIBUTI
    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaRepository statoFatturaRepository;
    private final StatoFatturaService statoFatturaService;

    public FatturaService(FatturaRepository fatturaRepository, ClienteRepository clienteRepository, StatoFatturaRepository statoFatturaRepository, StatoFatturaService statoFatturaService) {
        this.fatturaRepository = fatturaRepository;
        this.clienteRepository = clienteRepository;
        this.statoFatturaRepository = statoFatturaRepository;
        this.statoFatturaService = statoFatturaService;
    }

    //METODI

    //SALVA FATTURA
    @Transactional
    public Fattura saveFattura(FatturaDTO payload) {
        //1. trovo il cliente
        Cliente cliente = clienteRepository.findById(payload.idCliente()).orElseThrow(() -> new NotFoundException("il cliente con id " + payload.idCliente() + " non è stato trovato"));

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

    
}
