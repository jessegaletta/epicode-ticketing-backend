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
        //HTTP POST REQUEST TO https://api.eu.mailgun.net/v3/YOUR_DOMAIN_NAME/messages
        HttpResponse<JsonNode> response = Unirest.post("https://api.eu.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.sender)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Welcome on the platform!")
                .queryString("text", "Hello " + recipient.getFirstName() + ",\n\nregistration happened successfully.\n\nRegards,\nEpicode Ticketing")
                .asJson();
        System.out.println(response.getBody());
    }

    public void sendPasswordResetEmail(User recipient, String resetUrl){
        HttpResponse<JsonNode> response = Unirest.post("https://api.eu.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.sender)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Password Reset Request")
                .queryString("text", "Hello " + recipient.getFirstName() + ",\n\nYou have requested to reset your password. Click on the following link to set a new password:\n\n" + resetUrl + "\n\nIf you did not request this, please ignore this email.\n\nRegards,\nEpicode Ticketing")
                .asJson();
        System.out.println(response.getBody());
    }

    public void sendAccountLockedAdminNotification(User lockedUser, String adminEmail){
        HttpResponse<JsonNode> response = Unirest.post("https://api.eu.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.sender)
                .queryString("to", adminEmail)
                .queryString("subject", "SECURITY ALERT: User Account Locked")
                .queryString("text", "Hello Admin,\n\nThe account for user " + lockedUser.getEmail() + " (" + lockedUser.getFirstName() + " " + lockedUser.getLastName() + ") has been locked due to too many failed login attempts.\n\nRegards,\nEpicode Ticketing")
                .asJson();
        System.out.println(response.getBody());
    }
}
