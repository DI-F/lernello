package ch.nova_omnia.lernello.block.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameLearningUnitDTO(
    @NotBlank @Size(min = 3, max = 40) String name
    ) {
}
