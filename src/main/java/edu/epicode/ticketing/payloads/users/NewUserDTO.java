package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * REQUEST DTO: Used by a guest client to register a new account.
 * Endpoint: POST /auth/register
 * 
 * Contains strict validation rules. Requires a password. Does not include
 * settings or role management, as defaults are assigned automatically.
 */
public record NewUserDTO(
    @NotBlank(message = "First name is required!")
    @Size(min = 2, max = 30, message = "First name between 2 and 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required!")
    @Size(min = 2, max = 30, message = "Last name between 2 and 50 characters")
    String lastName,

    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password is required!")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    String password) {
}
