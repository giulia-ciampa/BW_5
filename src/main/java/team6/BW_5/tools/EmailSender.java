package team6.BW_5.tools;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team6.BW_5.entities.Utente;

@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;

    public EmailSender(@Value("${mailgun.domainName}") String domainName, @Value("${mailgun.apiKey}") String apiKey) {
        this.domainName = domainName;
        this.apiKey = apiKey;
    }

    public void sendCustomRegistrationEmail(Utente recipient, String messaggioPersonalizzato) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "admin@" + this.domainName)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Benvenuto sulla piattaforma!")
                .queryString("text", messaggioPersonalizzato)
                .asJson();

        System.out.println(response.getBody());
    }
}
