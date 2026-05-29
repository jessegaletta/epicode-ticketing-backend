package edu.epicode.ticketing.payloads.users;

import edu.epicode.ticketing.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * REQUEST DTO: Used by ADMIN to create or update other users.
 * Endpoints: POST /users, PUT /users/{userId}
 * 
 * Similar to UpdateUserProfileDTO, but includes the Role field so administrators
 * can assign or modify a user's privileges.
 */
public record UserProfileForAdminDTO(
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
        
        Long bachelorId,

        Role role
) {
}
