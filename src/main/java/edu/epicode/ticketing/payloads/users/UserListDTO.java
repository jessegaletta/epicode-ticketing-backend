package edu.epicode.ticketing.payloads.users;

import edu.epicode.ticketing.entities.Role;
import java.util.UUID;

/**
 * RESPONSE DTO: Used by the server to return a lightweight representation of a user for list views.
 * Endpoint: GET /users
 * 
 * Contains a dynamically calculated "isEditable" flag to tell the frontend if the current
 * authenticated user has permission to edit this specific record.
 */
public record UserListDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean isEditable
) {}
