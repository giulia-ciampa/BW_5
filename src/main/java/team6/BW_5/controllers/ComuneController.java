package team6.BW_5.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team6.BW_5.entities.Comune;
import team6.BW_5.exceptions.BadRequestException;
import team6.BW_5.services.ComuneService;

import java.util.List;

@RestController
@RequestMapping("/comuni")
public class ComuneController {

    private final ComuneService comuneService;

    public ComuneController(ComuneService comuneService) {
        this.comuneService = comuneService;
    }

    @GetMapping("/{siglaProvincia}")
    public List<Comune> findBySiglaProvincia(@PathVariable String siglaProvincia) {
        if (siglaProvincia.isBlank()) throw new BadRequestException("La sigla provincia non può essere vuota");
        return comuneService.findBySiglaProvincia(siglaProvincia);
    }


}
