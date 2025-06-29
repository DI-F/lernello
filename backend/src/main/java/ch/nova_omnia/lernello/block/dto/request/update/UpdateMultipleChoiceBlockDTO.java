package ch.nova_omnia.lernello.block.dto.request.update;

import static ch.nova_omnia.lernello.block.model.BlockType.MULTIPLE_CHOICE;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMultipleChoiceBlockDTO(
                                           @NotNull BlockType type,
                                           @NotNull UUID uuid,
                                           @NotBlank String name,
                                           String question,
                                           List<String> possibleAnswers,
                                           List<String> correctAnswers
) implements UpdateBlockDTO {
    public UpdateMultipleChoiceBlockDTO {
        type = MULTIPLE_CHOICE;
    }
}
