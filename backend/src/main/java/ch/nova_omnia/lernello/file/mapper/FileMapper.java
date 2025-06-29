package ch.nova_omnia.lernello.file.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.file.dto.response.FileResDTO;
import ch.nova_omnia.lernello.file.model.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResDTO toDTO(File file);
}