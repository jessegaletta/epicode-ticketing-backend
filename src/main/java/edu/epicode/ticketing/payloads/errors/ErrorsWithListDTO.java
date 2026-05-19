package edu.epicode.ticketing.payloads.errors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RESPONSE DTO: Used by the global ExceptionsHandler for validation errors.
 * Contains a human-readable message, a timestamp, and a list of specific field errors.
 */
public record ErrorsWithListDTO (String message, LocalDateTime timestamp, List<String> errors){
}
