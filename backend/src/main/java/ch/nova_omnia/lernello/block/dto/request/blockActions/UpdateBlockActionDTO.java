package ch.nova_omnia.lernello.block.dto.request.blockActions;

import static ch.nova_omnia.lernello.block.dto.request.blockActions.BlockActionType.UPDATE_BLOCK;

import java.util.List;

import ch.nova_omnia.lernello.block.dto.request.update.UpdateBlockDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateBlockActionDTO(
                                   @NotNull BlockActionType type,
                                   @NotNull String blockId,
                                   String content,
                                   String question,
                                   List<String> possibleAnswers,
                                   List<String> correctAnswers,
                                   String expectedAnswer,
                                   @Valid UpdateBlockDTO data
) implements BlockActionDTO {
    public UpdateBlockActionDTO {
        type = UPDATE_BLOCK;
    }
}

