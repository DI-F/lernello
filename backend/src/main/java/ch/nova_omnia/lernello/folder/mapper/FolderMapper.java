package ch.nova_omnia.lernello.folder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.folder.dto.request.CreateFolderDTO;
import ch.nova_omnia.lernello.folder.dto.response.FolderResDTO;
import ch.nova_omnia.lernello.folder.model.Folder;
import ch.nova_omnia.lernello.learningKit.mapper.LearningKitMapper;

@Mapper(componentModel = "spring", uses = {LearningKitMapper.class, ParentFolderMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FolderMapper {
    FolderResDTO toDTO(Folder folder);

    Folder toEntity(CreateFolderDTO createFolderDTO);
}
