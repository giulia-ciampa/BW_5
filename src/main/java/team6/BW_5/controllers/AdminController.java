package team6.BW_5.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.requestDTO.EmailDTO;
import team6.BW_5.services.UtenteService;
import team6.BW_5.tools.EmailSender;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final EmailSender emailSender;
    private final UtenteService utenteService;

    public AdminController(EmailSender emailSender, UtenteService utenteService) {
        this.emailSender = emailSender;
        this.utenteService = utenteService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {

        try {
            Utente utenteDB = utenteService.findByEmail(emailDTO.destinatario());
            if (utenteDB == null) {
                throw new BadRequestException("Utente con email " + emailDTO.destinatario() + " non trovato.");
            }
            emailSender.sendAdminCustomEmail(emailDTO.destinatario(), emailDTO.oggetto(), emailDTO.corpo());
            return ResponseEntity.ok("Email inviata con successo al destinatario" + " " + emailDTO.destinatario());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
