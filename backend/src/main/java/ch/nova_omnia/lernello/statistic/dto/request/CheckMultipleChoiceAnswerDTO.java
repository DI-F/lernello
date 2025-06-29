package ch.nova_omnia.lernello.statistic.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CheckMultipleChoiceAnswerDTO (
    @NotNull
    UUID blockId,
    @NotNull
    List<String> answers
) {
}
