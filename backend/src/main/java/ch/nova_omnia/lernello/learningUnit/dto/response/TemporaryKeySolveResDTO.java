package ch.nova_omnia.lernello.learningUnit.dto.response;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TemporaryKeySolveResDTO(
                                      @NotNull Map<String, UUID> temporaryKeyMap
) {
}
