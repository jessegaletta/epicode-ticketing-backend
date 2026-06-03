package edu.epicode.ticketing.payloads.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewTicketDTO(
        @NotBlank(message = "Title is mandatory")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @NotBlank(message = "Description is mandatory")
        String description,

        boolean isAnonymous,

        @NotBlank(message = "Category is mandatory")
        @Pattern(regexp = "ERROR|SUGGESTION|REQUEST|DOUBT", message = "Category must be one of: ERROR, SUGGESTION, REQUEST, DOUBT")
        String category,

        Long courseId,
        String moduleName,
        String lessonName,
        String expectedBenefit,
        String requestType,
        Boolean isFaqCandidate
) {
}
