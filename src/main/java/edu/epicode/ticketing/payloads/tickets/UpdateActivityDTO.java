package edu.epicode.ticketing.payloads.tickets;

import jakarta.validation.constraints.NotBlank;

public record UpdateActivityDTO(
        @NotBlank(message = "Text is required")
        String text
) {
}
