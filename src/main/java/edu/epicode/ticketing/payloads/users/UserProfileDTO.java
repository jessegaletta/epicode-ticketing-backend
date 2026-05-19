package edu.epicode.ticketing.payloads.users;

import edu.epicode.ticketing.entities.Role;

import java.util.UUID;

/**
 * RESPONSE DTO: Used by the server to send full user profile data back to the client.
 * Endpoints: GET /users/me, GET /users/{userId}, POST /users, PUT /users/...
 * 
 * Contains all read-only information needed by the frontend to display the user profile,
 * including auto-generated fields like ID and Avatar URL.
 * NEVER contains sensitive information like passwords.
 */
public record UserProfileDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String avatarURL,
        Role role,

        boolean darkMode,
        String timezone,
        String dateFormat,
        String timeFormat
) {
}
