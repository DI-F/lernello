package ch.nova_omnia.lernello.user.dto.response;

import java.util.UUID;

import ch.nova_omnia.lernello.user.model.Role;
import jakarta.validation.constraints.NotNull;

public record UserResDTO(
    @NotNull UUID uuid,
    @NotNull String username,
    @NotNull String name,
    @NotNull String surname,
    @NotNull Role role
) {
}
