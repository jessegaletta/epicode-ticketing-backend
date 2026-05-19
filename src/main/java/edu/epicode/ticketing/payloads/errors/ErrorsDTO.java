package edu.epicode.ticketing.payloads.errors;

import java.time.LocalDateTime;

/**
 * RESPONSE DTO: Used by the global ExceptionsHandler to return a standard error format.
 * Contains a human-readable message and a timestamp.
 */
public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
