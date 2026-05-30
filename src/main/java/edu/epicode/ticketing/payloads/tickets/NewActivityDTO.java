package edu.epicode.ticketing.payloads.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewActivityDTO(
        @NotBlank(message = "Text is required")
        String text,
        
        @NotNull(message = "Anonymous flag is required")
        Boolean isAnonymous
) {
}
