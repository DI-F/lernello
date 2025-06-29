package ch.nova_omnia.lernello.statistic.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TheoryBlockViewedResDTO (
    @NotNull
    UUID blockId,
    @NotNull
    Boolean isViewed
) {
}
