package ch.nova_omnia.lernello.user.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.user.dto.response.UserInfoDTO;
import ch.nova_omnia.lernello.user.model.User;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfoDTO toDTO(User user);
}
