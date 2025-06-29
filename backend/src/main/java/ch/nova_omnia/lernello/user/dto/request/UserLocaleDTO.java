package ch.nova_omnia.lernello.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLocaleDTO(
                            @NotBlank String locale
) {
}
