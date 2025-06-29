package ch.nova_omnia.lernello.statistic.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MultipleChoiceAnswerValidationResDTO(
    @NotNull
    UUID blockId,
    @NotNull
    boolean isCorrect
) {
}
