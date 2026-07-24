package team6.BW_5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team6.BW_5.entities.Cliente;
import team6.BW_5.entities.Indirizzo;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.FileNotSupportedException;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.exceptions.RecordAlreadyExistsException;
import team6.BW_5.exceptions.UnauthorizedException;
import team6.BW_5.repositories.ClienteRepository;
import team6.BW_5.requestDTO.ClienteDTO;
import team6.BW_5.requestDTO.ClienteFilterDTO;
import team6.BW_5.requestDTO.PatchAttivazioneClienteDTO;
import team6.BW_5.responseDTO.PatchAttivazioneClienteResponseDTO;
import team6.BW_5.specifications.ClienteSpecifications;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoService indirizzoService;
    private final ComuneService comuneService;
    private final ClienteSpecifications clienteSpecifications;
    private final Cloudinary fileUploader;


    public ClienteService(ClienteRepository clienteRepository, IndirizzoService indirizzoService, ComuneService comuneService, ClienteSpecifications clienteSpecifications, Cloudinary fileUploader) {
        this.clienteRepository = clienteRepository;
        this.indirizzoService = indirizzoService;
        this.comuneService = comuneService;
        this.clienteSpecifications = clienteSpecifications;
        this.fileUploader = fileUploader;
    }

    public void verifyClienteConditions(ClienteDTO body) {
        if (clienteRepository.existsByEmail(body.email()))
            throw new RecordAlreadyExistsException("Il cliente con email " + body.email() + " esiste già.");
        if (clienteRepository.existsByRagioneSociale(body.ragioneSociale()))
            throw new RecordAlreadyExistsException("Il cliente con ragione sociale " + body.ragioneSociale() + " esiste già.");
        if (clienteRepository.existsByPartitaIva(body.partitaIva()))
            throw new RecordAlreadyExistsException("Il cliente con partita IVA " + body.partitaIva() + " esiste già.");
        if (clienteRepository.existsByPec(body.pec()))
            throw new RecordAlreadyExistsException("Il cliente con PEC " + body.pec() + " esiste già.");
    }

    public Page<Cliente> findAll(int page, int size, String sortBy, Sort.Direction direction, ClienteFilterDTO filters) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Cliente> spec = clienteSpecifications.specificationClienteBuilder(filters);

        if (spec == null) {
            return clienteRepository.findAll(pageable);
        }

        return clienteRepository.findAll(spec, pageable);
    }

    public Cliente createCliente(ClienteDTO body, Utente utente) {
        verifyClienteConditions(body);

        Indirizzo sedeLegale = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeLegale(), body.civicoSedeLegale(), body.localitaSedeLegale(), body.capSedeLegale(), body.denominazioneComuneSedeLegale(), body.siglaProvinciaSedeLegale());

        Indirizzo sedeOperativa = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeOperativa(), body.civicoSedeOperativa(), body.localitaSedeOperativa(), body.capSedeOperativa(), body.denominazioneComuneSedeOperativa(), body.siglaProvinciaSedeOperativa());

        return clienteRepository.save(new Cliente(body.ragioneSociale(), body.partitaIva(), body.email(), body.fatturatoAnnuale(), body.pec(), body.telefono(), body.emailContatto(), body.nomeContatto(), body.cognomeContatto(), body.telefonoContatto(), body.tipo(), utente, sedeLegale, sedeOperativa));
    }

    public Cliente findById(UUID clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new NotFoundException("Cliente con id '" + clienteId + "' non trovato"));
    }

    public Page<Cliente> findOwnClienti(int page, int size, String sortBy, Sort.Direction direction, Utente utente, ClienteFilterDTO filters) {
        if (size <= 0) size = 10;
        if (size > 20) size = 20;
        if (page < 0) page = 0;
        Specification<Cliente> spec = clienteSpecifications.specificationClienteBuilder(filters);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        if (spec == null) {
            return clienteRepository.findByUtente(utente, pageable);
        }
        spec = spec.and(clienteSpecifications.byUtente(utente));
        return clienteRepository.findAll(spec, pageable);
    }

    public List<Cliente> findOwnClienti(Utente utente) {
        return clienteRepository.findByUtente(utente);
    }

    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Il cliente con email '" + email + "' non è stato trovato."));
    }

    public Cliente updateCliente(ClienteDTO body, Utente utente, UUID clienteId) {

        Cliente cliente = findById(clienteId);

        if (!utente.getUtenteId().equals(cliente.getUtente().getUtenteId()))
            throw new UnauthorizedException("Non possiedi l'autorizzazione per modificare questo cliente.");


        Indirizzo sedeLegale;
        Indirizzo sedeOperativa;

        if (cliente.getSedeLegale().getCap().equals(body.capSedeLegale()) && cliente.getSedeLegale().getVia().equals(body.viaSedeLegale()) && cliente.getSedeLegale().getCivico().equals(body.civicoSedeLegale()) && cliente.getSedeLegale().getComune().equals(comuneService.findByDenominazioneAndProvincia(body.denominazioneComuneSedeLegale(), body.siglaProvinciaSedeLegale())) && cliente.getSedeLegale().getLocalita().equals(body.localitaSedeLegale())) {
            sedeLegale = cliente.getSedeLegale();
        } else
            sedeLegale = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeLegale(), body.civicoSedeLegale(), body.localitaSedeLegale(), body.capSedeLegale(), body.denominazioneComuneSedeLegale(), body.siglaProvinciaSedeLegale());

        if (cliente.getSedeOperativa().getCap().equals(body.capSedeOperativa()) && cliente.getSedeOperativa().getVia().equals(body.viaSedeOperativa()) && cliente.getSedeOperativa().getCivico().equals(body.civicoSedeOperativa()) && cliente.getSedeOperativa().getComune().equals(comuneService.findByDenominazioneAndProvincia(body.denominazioneComuneSedeOperativa(), body.siglaProvinciaSedeOperativa())) && cliente.getSedeOperativa().getLocalita().equals(body.localitaSedeOperativa())) {
            sedeOperativa = cliente.getSedeOperativa();
        } else
            sedeOperativa = indirizzoService.findByViaCivicoLocalitaOptionalAndComune(body.viaSedeOperativa(), body.civicoSedeOperativa(), body.localitaSedeOperativa(), body.capSedeOperativa(), body.denominazioneComuneSedeOperativa(), body.siglaProvinciaSedeOperativa());


        if (!cliente.getRagioneSociale().equals(body.ragioneSociale())) {
            if (clienteRepository.existsByRagioneSociale(body.ragioneSociale()))
                throw new RecordAlreadyExistsException("Il cliente con ragione sociale " + body.ragioneSociale() + " esiste già.");
            cliente.setRagioneSociale(body.ragioneSociale());
        }
        if (!cliente.getPartitaIva().equals(body.partitaIva())) {
            if (clienteRepository.existsByPartitaIva(body.partitaIva()))
                throw new RecordAlreadyExistsException("Il cliente con partita IVA " + body.partitaIva() + " esiste già.");
            cliente.setPartitaIva(body.partitaIva());
        }
        if (!cliente.getEmail().equals(body.email())) {
            if (clienteRepository.existsByEmail(body.email()))
                throw new RecordAlreadyExistsException("Il cliente con email " + body.email() + " esiste già.");
            cliente.setEmail(body.email());
        }
        if (!cliente.getPec().equals(body.pec())) {
            if (clienteRepository.existsByPec(body.pec()))
                throw new RecordAlreadyExistsException("Il cliente con PEC " + body.pec() + " esiste già.");
            cliente.setPec(body.pec());
        }
        if (cliente.getFatturatoAnnuale() != body.fatturatoAnnuale())
            cliente.setFatturatoAnnuale(body.fatturatoAnnuale());
        if (!cliente.getTelefono().equals(body.telefono())) cliente.setTelefono(body.telefono());
        if (!cliente.getEmailContatto().equals(body.emailContatto())) cliente.setEmailContatto(body.emailContatto());
        if (!cliente.getNomeContatto().equals(body.nomeContatto())) cliente.setNomeContatto(body.nomeContatto());
        if (!cliente.getCognomeContatto().equals(body.cognomeContatto()))
            cliente.setCognomeContatto(body.cognomeContatto());
        if (!cliente.getTelefonoContatto().equals(body.telefonoContatto()))
            cliente.setTelefonoContatto(body.telefonoContatto());
        if (!cliente.getTipo().equals(body.tipo())) cliente.setTipo(body.tipo());
        if (!cliente.getSedeLegale().equals(sedeLegale)) cliente.setSedeLegale(sedeLegale);
        if (!cliente.getSedeOperativa().equals(sedeOperativa)) cliente.setSedeOperativa(sedeOperativa);


        return clienteRepository.save(cliente);

    }

    public void deleteOwnCliente(Utente utenteAutenticato, UUID clienteId) {
        Cliente cliente = findById(clienteId);

        if (!utenteAutenticato.getUtenteId().equals(cliente.getUtente().getUtenteId()))
            throw new UnauthorizedException("Non possiedi l'autorizzazione per cancellare questo cliente.");

        clienteRepository.delete(cliente);
    }

    public PatchAttivazioneClienteResponseDTO setIsAttivo(Utente utenteAutenticato, UUID clienteId, PatchAttivazioneClienteDTO body) {
        Cliente cliente = findById(clienteId);
        if (cliente.getUtente().getUtenteId().equals(utenteAutenticato.getUtenteId()) || utenteAutenticato.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ADMIN"))) {
            cliente.setAttivo(body.isAttivo());
            Cliente saved = clienteRepository.save(cliente);
            return new PatchAttivazioneClienteResponseDTO(saved.getRagioneSociale(), saved.isAttivo());
        } else throw new UnauthorizedException("Non possiedi le autorizzazioni per modificare questo cliente.");
    }

    public Cliente setLogoCliente(Utente utenteAutenticato, UUID clienteId, MultipartFile logo) {
        Cliente cliente = findById(clienteId);
        if (utenteAutenticato.getUtenteId().equals(cliente.getUtente().getUtenteId()) || utenteAutenticato.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ADMIN"))) {
            if (logo.getSize() > 10485760) throw new FileNotSupportedException("File's size can't be more than 10MB");
            if (!(Objects.equals(logo.getContentType(), "image/jpeg") || Objects.equals(logo.getContentType(), "image/png")))
                throw new FileNotSupportedException("Sono ammesse solo immagini jpeg o png ammesse");

            try {
                Map result = fileUploader.uploader().upload(logo.getBytes(), ObjectUtils.emptyMap());
                String url = (String) result.get("secure_url");

                cliente.setLogoAziendale(url);
                clienteRepository.save(cliente);

                return cliente;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else throw new UnauthorizedException("Non possiedi i permessi per cambiare questo logo.");
    }
}
