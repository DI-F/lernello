package ch.nova_omnia.lernello.block.dto.request.blocks;

import ch.nova_omnia.lernello.block.model.BlockLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TranslatedBlockDTO(
    @NotNull UUID id,
    @NotBlank BlockLanguage language,
    String name,
    String content,
    String question,
    String expectedAnswer,
    List<String> possibleAnswers,
    List<String> correctAnswers
) {

}
