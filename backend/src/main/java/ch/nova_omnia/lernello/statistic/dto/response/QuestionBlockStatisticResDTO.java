package ch.nova_omnia.lernello.statistic.dto.response;

import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.NotNull;

public record QuestionBlockStatisticResDTO(
    @NotNull
    UUID blockId,
    String lastAnswer,
    Boolean isCorrect
) implements BlockStatisticResDTO {
    @Override
    public BlockType blockType() {
        return BlockType.QUESTION;
    }
}
