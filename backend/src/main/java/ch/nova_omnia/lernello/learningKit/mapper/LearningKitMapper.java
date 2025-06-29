package ch.nova_omnia.lernello.learningKit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.file.service.FileService;
import ch.nova_omnia.lernello.learningKit.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.learningKit.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.learningKit.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.learningUnit.mapper.LearningUnitMapper;
import ch.nova_omnia.lernello.user.service.UserService;

@Mapper(
        componentModel = "spring", uses = {LearningUnitMapper.class, JsonNullableMapper.class, FileService.class, UserService.class
        }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LearningKitMapper {
    LearningKitResDTO toDTO(LearningKit learningKit);

    LearningKit toEntity(CreateLearningKitDTO createLearningKitResDto);

    void update(UpdateLearningKitDTO source, @MappingTarget LearningKit destination);
}
