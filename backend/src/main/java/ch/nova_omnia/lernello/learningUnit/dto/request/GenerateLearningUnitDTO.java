package ch.nova_omnia.lernello.learningUnit.dto.request;

import java.util.List;
import java.util.UUID;

public record GenerateLearningUnitDTO(
                                      List<UUID> fileIds,
                                      String prompt,
                                      String difficulty,
                                      boolean includeTheory,
                                      boolean includeQuestions,
                                      boolean includeMultipleChoice
) {
}
