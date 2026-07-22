package team6.BW_5.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Utente;
import team6.BW_5.exceptions.NotFoundException;
import team6.BW_5.requestDTO.UtenteRequestDTO;
import team6.BW_5.services.AssegnazioneRuoloService;
import team6.BW_5.services.RuoloUtenteService;
import team6.BW_5.services.UtenteService;

@Component
@Order(2)
public class AdminRunner implements CommandLineRunner {

    private final UtenteService utenteService;
    private final RuoloUtenteService ruoloUtenteService;
    private final AssegnazioneRuoloService assegnazioneRuoloService;

    public AdminRunner(UtenteService utenteService, RuoloUtenteService ruoloUtenteService, AssegnazioneRuoloService assegnazioneRuoloService) {
        this.utenteService = utenteService;
        this.ruoloUtenteService = ruoloUtenteService;
        this.assegnazioneRuoloService = assegnazioneRuoloService;
    }

    @Override
    public void run(String... args) throws Exception {
        creaAdmin();
    }

    public void creaAdmin() {
        Utente admin;
        try {
            admin = utenteService.findByEmail("admin@admin.com");
        } catch (NotFoundException ex) {
            admin = utenteService.salvaUtente(new UtenteRequestDTO("Admin", "admin@admin.com", "Admin123", "Admin", "Admin"));
        }

        try {
            assegnazioneRuoloService.assegnaRuolo(admin, ruoloUtenteService.findByNomeRuolo("ADMIN"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("Ruoli utente admin: " + admin.getAuthorities());
        }

    }


}
