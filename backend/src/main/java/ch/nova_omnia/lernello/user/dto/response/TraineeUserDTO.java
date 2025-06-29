package ch.nova_omnia.lernello.user.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TraineeUserDTO(
                                 @NotNull UUID uuid,
                                 @NotNull String username,
                                 @NotNull String name,
                                 @NotNull String surname
) {
}
