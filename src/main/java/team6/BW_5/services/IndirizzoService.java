package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.repositories.IndirizzoRepository;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;


    public IndirizzoService(IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
    }
}
