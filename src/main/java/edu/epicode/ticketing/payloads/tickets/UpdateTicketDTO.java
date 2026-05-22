package edu.epicode.ticketing.payloads.tickets;

import edu.epicode.ticketing.entities.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTicketDTO(
        @NotBlank(message = "Title is mandatory")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Status is mandatory")
        TicketStatus status
) {
}
