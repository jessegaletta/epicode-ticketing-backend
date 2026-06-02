package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record ForgotPasswordDTO (
    @NotEmpty(message = "Email is mandatory")
    @Email(message = "The provided email is not valid")
    String email
) {}
