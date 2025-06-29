package ch.nova_omnia.lernello.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
                           @NotBlank @Email String username,
                           @NotBlank @Size(min = 8) String password
) {
}
