package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * REQUEST DTO: Used by the client to submit login credentials.
 * Endpoint: POST /auth/login
 * 
 * Contains validation for email and password.
 */
public record LoginDTO (
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email, 
        
        @NotBlank(message = "Password is required")
        String password
) {
}
