package ch.nova_omnia.lernello.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDataDTO(
                                    @NotBlank @Size(min = 8) String newPassword
) {
}
