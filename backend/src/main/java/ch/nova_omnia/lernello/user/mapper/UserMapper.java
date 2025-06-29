package ch.nova_omnia.lernello.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.user.dto.request.UpdateUserDTO;
import ch.nova_omnia.lernello.user.dto.response.UserResDTO;
import ch.nova_omnia.lernello.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResDTO toDTO(User user);

    User toEntity(UpdateUserDTO userDTO);
}
