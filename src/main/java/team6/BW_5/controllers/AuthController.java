package team6.BW_5.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.ValidationException;
import team6.BW_5.requestDTO.LoginDTO;
import team6.BW_5.requestDTO.UtenteRequestDTO;
import team6.BW_5.responseDTO.LoginResponseDTO;
import team6.BW_5.responseDTO.UtenteResponseDTO;
import team6.BW_5.services.AuthService;
import team6.BW_5.services.UtenteService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UtenteService utenteService;
    private final AuthService authService;

    public AuthController(UtenteService utenteService, AuthService authService) {
        this.utenteService = utenteService;
        this.authService = authService;
    }

    //REGISTRAZIONE UTENTE
    @PostMapping("/registrazione")
    public UtenteResponseDTO registrazione(@RequestBody @Validated UtenteRequestDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }


        Utente utenteRegistrato = utenteService.salvaUtente(body);
        return new UtenteResponseDTO(
                utenteRegistrato.getUtenteId(),
                utenteRegistrato.getUsername(),
                utenteRegistrato.getEmail(),
                utenteRegistrato.getNome(),
                utenteRegistrato.getCognome()
        );
    }


    //LOGIN
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO payload) {
        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }
}
