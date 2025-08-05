package ch.nova_omnia.lernello.learningUnit.dto.request;

import ch.nova_omnia.lernello.block.dto.request.blocks.BlockDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record SaveLearningUnitDTO (
    @NotNull UUID uuid,
    @NotBlank @Size(min = 3, max = 40) String name,
    @NotNull List<BlockDTO> blocks,
    @Min(0) int position
) {

}
