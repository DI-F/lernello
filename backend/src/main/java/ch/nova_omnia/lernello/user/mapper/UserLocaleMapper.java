package ch.nova_omnia.lernello.user.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.user.dto.request.UserLocaleDTO;

@Mapper(componentModel = "spring")
public interface UserLocaleMapper {
    UserLocaleDTO toDTO(String locale);
}
