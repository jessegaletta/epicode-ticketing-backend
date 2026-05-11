package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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


        boolean darkMode,
        String timezone
) {
}
