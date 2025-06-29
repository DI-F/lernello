package ch.nova_omnia.lernello.user.dto.response;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoggedInUserDTO(
                              @NotBlank String token,
                              @NotNull ZonedDateTime expires
) {
}
