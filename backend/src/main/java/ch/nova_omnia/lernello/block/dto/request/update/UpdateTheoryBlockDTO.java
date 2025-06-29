package ch.nova_omnia.lernello.block.dto.request.update;

import static ch.nova_omnia.lernello.block.model.BlockType.THEORY;

import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTheoryBlockDTO(
                                   @NotNull BlockType type,
                                   @NotNull UUID uuid,
                                   @NotBlank String name,
                                   String content
) implements UpdateBlockDTO {
    public UpdateTheoryBlockDTO {
        type = THEORY;
    }
}
