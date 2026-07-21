package team6.BW_5.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Comune;
import team6.BW_5.entities.Provincia;
import team6.BW_5.services.ComuneService;
import team6.BW_5.services.ProvinciaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatiGeograficiRunner implements CommandLineRunner {

    private static final Map<String, String> aliasProvince = Map.ofEntries(
            Map.entry("Verbano-Cusio-Ossola", "Verbania"),
            Map.entry("Pesaro e Urbino", "Pesaro-Urbino"),
            Map.entry("Monza e della Brianza", "Monza-Brianza"),
            Map.entry("Bolzano/Bozen", "Bolzano"),
            Map.entry("Reggio nell'Emilia", "Reggio-Emilia"),
            Map.entry("Valle d'Aosta/Vallée d'Aoste", "Aosta"),
            Map.entry("Forlì-Cesena", "Forli-Cesena")
    );
    public final ComuneService comuneService;
    public final ProvinciaService provinciaService;

    public DatiGeograficiRunner(ComuneService comuneService, ProvinciaService provinciaService) {
        this.comuneService = comuneService;
        this.provinciaService = provinciaService;
    }

    private static String normalizza(String s) {
        return s.replace("-", " ").replaceAll("\\s+", " ").trim().toLowerCase();
    }

    @Override
    public void run(String... args) throws Exception {
        if (provinciaService.count() == 0) {
            caricaProvince();
        }
        if (comuneService.count() == 0) {
            caricaComuni();
        }
    }

    private void caricaProvince() throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("province-italiane.csv").getInputStream(), StandardCharsets.UTF_8))) {

            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.isBlank()) continue;
                String[] campi = riga.split(";");
                String sigla = campi[0].trim();
                String nome = campi[1].trim();
                String regione = campi[2].trim();

                provinciaService.save(new Provincia(sigla, nome, regione));
            }
        }
    }

    private void caricaComuni() throws Exception {
        Map<String, Provincia> mappaProvince = new HashMap<>();
        for (Provincia p : provinciaService.findAll()) {
            mappaProvince.put(normalizza(p.getNome()), p);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("comuni-italiani.csv").getInputStream(), StandardCharsets.UTF_8))) {

            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.isBlank()) continue;
                String[] campi = riga.split(";");

                int codiceProvincia;
                int progressivoComune;
                try {
                    codiceProvincia = Integer.parseInt(campi[0].trim());
                    progressivoComune = Integer.parseInt(campi[1].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Riga scartata, dati numerici non validi: " + riga);
                    codiceProvincia = 0;
                    progressivoComune = 0;
                    continue;
                }
                String denominazioneComune = campi[2].trim();
                String nomeProvinciaGrezzo = campi[3].trim();

                Provincia provincia = trovaOCreaProvincia(nomeProvinciaGrezzo, mappaProvince);

                if (provincia == null) {
                    System.out.println("Provincia non trovata per '" + nomeProvinciaGrezzo
                            + "' (comune: " + denominazioneComune + ") - saltato");
                    continue;
                }

                comuneService.save(new Comune(codiceProvincia, progressivoComune, denominazioneComune, provincia));
            }
        }
    }

    private Provincia trovaOCreaProvincia(String nomeProvinciaGrezzo, Map<String, Provincia> mappaProvince) {
        String nomeDaCercare = aliasProvince.getOrDefault(nomeProvinciaGrezzo, nomeProvinciaGrezzo);
        Provincia provincia = mappaProvince.get(normalizza(nomeDaCercare));

        if (provincia == null && nomeProvinciaGrezzo.equals("Sud Sardegna")) {
            provincia = provinciaService.save(new Provincia("SU", "Sud Sardegna", "Sardegna"));
            mappaProvince.put(normalizza("Sud Sardegna"), provincia);
        }

        return provincia;
    }
}
