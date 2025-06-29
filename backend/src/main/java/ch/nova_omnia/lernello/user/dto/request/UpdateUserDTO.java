package ch.nova_omnia.lernello.user.dto.request;

import ch.nova_omnia.lernello.user.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
    @Size(min = 3, max = 40)
    @NotNull String name,
    @Size(min = 3, max = 40)
    @NotNull String surname,
    @NotNull Role role
) {
}
