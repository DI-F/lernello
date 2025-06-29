package ch.nova_omnia.lernello.learningKit.dto.response;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.file.dto.response.FileResDTO;
import ch.nova_omnia.lernello.learningUnit.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.user.dto.response.TraineeUserDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LearningKitResDTO(
                                @NotNull UUID uuid,
                                @NotNull @Size(min = 3, max = 40) String name,
                                List<LearningUnitResDTO> learningUnits,
                                String description,
                                ZonedDateTime deadlineDate,
                                boolean published,
                                List<TraineeUserDTO> trainees,
                                List<FileResDTO> files,
                                @NotNull int averageProgress,
                                @NotNull int completionRate
) {
}
