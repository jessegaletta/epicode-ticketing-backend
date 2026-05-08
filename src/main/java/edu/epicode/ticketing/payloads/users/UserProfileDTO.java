package edu.epicode.ticketing.payloads.users;

import edu.epicode.ticketing.entities.Role;

import java.util.UUID;

public record UserProfileDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String avatarURL,
        Role role,
        boolean emailNotifications,
        boolean darkMode,
        String timezone
) {
}
