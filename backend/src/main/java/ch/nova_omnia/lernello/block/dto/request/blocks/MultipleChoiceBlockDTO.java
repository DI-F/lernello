package ch.nova_omnia.lernello.block.dto.request.blocks;

import ch.nova_omnia.lernello.block.model.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

import static ch.nova_omnia.lernello.block.model.BlockType.MULTIPLE_CHOICE;

public record MultipleChoiceBlockDTO(
                                        @NotNull BlockType type,
                                        @NotNull UUID uuid,
                                        @Size(min = 3, max = 40) @NotBlank String name,
                                        @Min(0) int position,
                                        @NotBlank String question,
                                        @NotNull List<String> possibleAnswers,
                                        @NotNull List<String> correctAnswers,
                                        @NotNull List<TranslatedBlockDTO> translatedContents
) implements BlockDTO {
    public MultipleChoiceBlockDTO {
        type = MULTIPLE_CHOICE;
    }
}
