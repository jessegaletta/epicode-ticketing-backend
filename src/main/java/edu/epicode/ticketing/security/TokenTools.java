package edu.epicode.ticketing.security;

import edu.epicode.ticketing.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TokenTools {

    private final String secret;

    public TokenTools(@Value("${jwt.secret}") String secret){
        this.secret = secret;
    }

    public String generateToken(User user){
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())) // IAT Issued AT
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // Expiration Date (15 minutes)
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String accessToken){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(accessToken);
    }

    public UUID extractIdFromToken(String accessToken){
        return UUID.fromString(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject());
    }
}
