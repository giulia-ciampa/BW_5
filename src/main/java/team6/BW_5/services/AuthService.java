package team6.BW_5.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.UnauthorizedException;
import team6.BW_5.requestDTO.LoginDTO;
import team6.BW_5.security.JWTTools;

@Service
public class AuthService {
    private final PasswordEncoder bcrypt;
    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    public AuthService(PasswordEncoder bcrypt, JWTTools jwtTools, UtenteService utenteService) {
        this.bcrypt = bcrypt;
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    //CONTROLLO CREDENZIALI E GENERA TOKEN

    public String checkCredentialsAndGenerateToken(LoginDTO payload) {
        //email
        Utente utenteTrovato = utenteService.findByEmail(payload.email());

        //password
        if (!this.bcrypt.matches(payload.password(), utenteTrovato.getPassword())) {
            return this.jwtTools.generateToken(utenteTrovato);
        } else {
            throw new UnauthorizedException("credenziali sbagliate");
        }
    }

}
