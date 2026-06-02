package edu.epicode.ticketing.payloads.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO (
    @NotEmpty(message = "Token is mandatory")
    String token,
    
    @NotEmpty(message = "New password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String newPassword
) {}
