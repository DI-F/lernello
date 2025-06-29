package ch.nova_omnia.lernello.user.dto.response;

import java.util.UUID;

import ch.nova_omnia.lernello.user.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoDTO(
                          String locale,
                          @NotBlank String username,
                          @NotNull UUID uuid,
                          boolean changedPassword,
                          @NotNull Role role
) {
}
