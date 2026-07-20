package team6.BW_5.services;

import org.springframework.stereotype.Service;
import team6.BW_5.repositories.SedeRepository;

@Service
public class SedeService {

    private final SedeRepository sedeRepository;


    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }
}
