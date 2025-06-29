package ch.nova_omnia.lernello.statistic.dto.response;

import static ch.nova_omnia.lernello.block.model.BlockType.THEORY;

import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.NotNull;

public record TheoryBlockStatisticResDTO(
    @NotNull
    UUID blockId,
    @NotNull
    Boolean isViewed
) implements BlockStatisticResDTO {
    @Override
    public BlockType blockType() {
        return THEORY;
    }
}
