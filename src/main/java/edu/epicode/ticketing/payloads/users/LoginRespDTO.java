package edu.epicode.ticketing.payloads.users;

/**
 * RESPONSE DTO: Used by the server to return the JWT token upon successful authentication.
 * Endpoint: POST /auth/login
 */
public record LoginRespDTO(String accessToken) {
}
