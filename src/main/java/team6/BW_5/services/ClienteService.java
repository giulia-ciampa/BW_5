package team6.BW_5.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.RecordAlreadyExistsException;
import team6.BW_5.repositories.ClienteRepository;
import team6.BW_5.requestDTO.ClienteDTO;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoService indirizzoService;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoService indirizzoService) {
        this.clienteRepository = clienteRepository;
        this.indirizzoService = indirizzoService;
    }

    public Page<Cliente> findAll(int page, int size, String sortBy, Sort.Direction direction) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return clienteRepository.findAll(pageable);
    }

    public Cliente createCliente(ClienteDTO body, Utente utente) {
        if (clienteRepository.existsByEmail(body.email()))
            throw new RecordAlreadyExistsException("Il cliente con email " + body.email() + " esiste già.");
        if (clienteRepository.existsByPartitaIva(body.ragioneSociale()))
            throw new RecordAlreadyExistsException("Il cliente con ragione sociale " + body.ragioneSociale() + " esiste già.");
        if (clienteRepository.existsByPartitaIva(body.partitaIva()))
            throw new RecordAlreadyExistsException("Il cliente con partita IVA " + body.partitaIva() + " esiste già.");
        if (clienteRepository.existsByPec(body.pec()))
            throw new RecordAlreadyExistsException("Il cliente con PEC " + body.pec() + " esiste già.");


        Indirizzo sedeLegale = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeLegale(), body.civicoSedeLegale(), body.localitaSedeLegale(), body.capSedeLegale(), body.denominazioneComuneSedeLegale(), body.siglaProvinciaSedeLegale());

        Indirizzo sedeOperativa = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeOperativa(), body.civicoSedeOperativa(), body.localitaSedeOperativa(), body.capSedeOperativa(), body.denominazioneComuneSedeOperativa(), body.siglaProvinciaSedeOperativa());

        return clienteRepository.save(new Cliente(body.ragioneSociale(), body.partitaIva(), body.email(), body.fatturatoAnnuale(), body.pec(), body.telefono(), body.emailContatto(), body.nomeContatto(), body.cognomeContatto(), body.telefonoContatto(), body.tipo(), utente, sedeLegale, sedeOperativa));
    }

    public Cliente findById(UUID clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new NotFoundException("Cliente con id '" + clienteId + "' non trovato"));
    }
}
