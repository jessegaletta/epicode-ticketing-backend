package edu.epicode.ticketing.payloads.errors;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
