package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

/**
 * REQUEST DTO: Used by the client to update their own personal profile.
 * Endpoint: PUT /users/me
 * 
 * Contains validation rules for incoming data. It does not include the user ID
 * or avatar URL, as those are handled separately or auto-generated.
 * It does not include Role, as standard users cannot elevate their own privileges.
 */
public record UpdateUserProfileDTO(
        @NotBlank(message = "First name is required!")
        @Size(min = 2, max = 30, message = "First name between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required!")
        @Size(min = 2, max = 30, message = "Last name between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Email is required!")
        @Email(message = "Email must be valid")
        String email,

        @Pattern(regexp = "^$|^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password too weak")
        String password,

        boolean darkMode,
        String timezone,
        String dateFormat,
        String timeFormat,

        @NotNull(message = "Bachelor is required!")
        Long bachelorId
) {
}
