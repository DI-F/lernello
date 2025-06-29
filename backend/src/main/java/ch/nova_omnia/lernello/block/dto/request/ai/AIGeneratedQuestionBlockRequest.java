package ch.nova_omnia.lernello.block.dto.request.ai;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AIGeneratedQuestionBlockRequest(
                                              @NotNull UUID theoryBlockId,
                                              @NotNull UUID blockId
) {
}
