package edu.epicode.ticketing.payloads.tickets;

import edu.epicode.ticketing.entities.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusDTO(
        @NotNull(message = "Status is mandatory")
        TicketStatus status,
        
        String comment
) {
}
