package ch.nova_omnia.lernello.statistic.dto.response;

import static ch.nova_omnia.lernello.block.model.BlockType.MULTIPLE_CHOICE;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.NotNull;

public record MultipleChoiceBlockStatisticResDTO(
    @NotNull
    UUID blockId,
    List<String> lastAnswers,
    Boolean isCorrect
) implements BlockStatisticResDTO {
    @Override
    public BlockType blockType() {
        return MULTIPLE_CHOICE;
    }
}
