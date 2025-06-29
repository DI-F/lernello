package ch.nova_omnia.lernello.statistic.dto.response;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LearningUnitStatisticResDTO(
    @NotNull
    String learningUnitId,
    @NotNull
    boolean isOpened,
    @NotNull
    boolean isCompleted,
    ZonedDateTime firstOpenedAt,
    ZonedDateTime lastOpenedAt,
    ZonedDateTime completedAt,
    @NotNull
    @Min(0)
    @Max(100)
    int progressPercentage,
    @NotNull
    List<BlockStatisticResDTO> userBlockProgresses
) {
}
