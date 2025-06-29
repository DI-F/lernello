package ch.nova_omnia.lernello.block.dto.response;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.block.model.BlockLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TranslatedBlockResDTO(
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
