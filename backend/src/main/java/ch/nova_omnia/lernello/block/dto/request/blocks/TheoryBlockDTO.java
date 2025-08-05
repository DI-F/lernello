package ch.nova_omnia.lernello.block.dto.request.blocks;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

import static ch.nova_omnia.lernello.block.model.BlockType.THEORY;


public record TheoryBlockDTO(
                                @NotNull BlockType type,
                                UUID uuid,
                                @Size(min = 3, max = 40) @NotBlank String name,
                                @Min(0) int position,
                                @NotBlank String content,
                                @NotNull List<TranslatedBlockDTO> translatedContents
) implements BlockDTO {
    public TheoryBlockDTO {
        type = THEORY;
    }
}
