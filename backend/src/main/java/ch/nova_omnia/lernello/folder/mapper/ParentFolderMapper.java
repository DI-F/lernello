package ch.nova_omnia.lernello.folder.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.folder.dto.response.ParentFolderResDTO;
import ch.nova_omnia.lernello.folder.model.Folder;

@Mapper(componentModel = "spring")
public interface ParentFolderMapper {
    ParentFolderResDTO toDTO(Folder folder);
}
