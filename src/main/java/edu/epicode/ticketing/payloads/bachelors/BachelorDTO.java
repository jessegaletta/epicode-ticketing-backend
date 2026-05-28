package edu.epicode.ticketing.payloads.bachelors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BachelorDTO(
        @NotBlank(message = "Description is required!") @Size(min = 2, max = 50, message = "Description must be between 2 and 50 characters") String description) {
}
