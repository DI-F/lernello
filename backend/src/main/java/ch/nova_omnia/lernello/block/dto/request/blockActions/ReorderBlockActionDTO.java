package ch.nova_omnia.lernello.block.dto.request.blockActions;

import static ch.nova_omnia.lernello.block.dto.request.blockActions.BlockActionType.REORDER_BLOCK;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReorderBlockActionDTO(
                                    @NotNull BlockActionType type,
                                    @NotBlank String blockId,
                                    @NotNull @Min(0) int newIndex
) implements BlockActionDTO {
    public ReorderBlockActionDTO {
        type = REORDER_BLOCK;
    }
}

