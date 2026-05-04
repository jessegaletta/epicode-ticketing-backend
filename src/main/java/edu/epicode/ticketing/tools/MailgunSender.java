package edu.epicode.ticketing.tools;

import edu.epicode.ticketing.entities.User;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {

    private String domainName;
    private String apiKey;
    private String sender;

    public MailgunSender(@Value("${mailgun.domain}") String domainName,
                         @Value("${mailgun.apiKey}") String apiKey,
                         @Value("${mailgun.sender}") String sender){
        this.domainName = domainName;
        this.apiKey = apiKey;
        this.sender = sender;
    }

    public void sendRegistrationEmail(User recipient){
        //HTTP POST REQUEST TO https://api.mailgun.net/v3/YOUR_DOMAIN_NAME/messages
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.sender)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Welcome on the platform!")
                .queryString("test", "Hello, " + recipient.getFirstName() + ", registration happened successfully!")
                .asJson();
        System.out.println(response.getBody());
    }
}
