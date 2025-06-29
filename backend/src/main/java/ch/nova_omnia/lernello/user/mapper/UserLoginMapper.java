package ch.nova_omnia.lernello.user.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.user.dto.response.LoggedInUserDTO;
import ch.nova_omnia.lernello.user.model.User;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    LoggedInUserDTO toDTO(User user);
}
