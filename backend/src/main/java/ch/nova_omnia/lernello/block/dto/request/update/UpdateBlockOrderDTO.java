package ch.nova_omnia.lernello.block.dto.request.update;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;


public record UpdateBlockOrderDTO(
                                  @NotNull List<UUID> blockUuidsInOrder
) {
}