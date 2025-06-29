package ch.nova_omnia.lernello.statistic.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record LearningKitOpened(
    @NotNull
    UUID learningKitId
) {
}
