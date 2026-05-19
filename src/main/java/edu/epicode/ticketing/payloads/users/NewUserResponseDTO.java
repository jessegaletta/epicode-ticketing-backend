package edu.epicode.ticketing.payloads.users;

import java.util.UUID;

/**
 * RESPONSE DTO: Used by the server to confirm successful registration.
 * Endpoint: POST /auth/register
 * 
 * Returns the UUID of the newly created user instead of the full user object for security.
 */
public record NewUserResponseDTO(UUID userId) {
}
