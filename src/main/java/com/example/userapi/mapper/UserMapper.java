package com.example.userapi.mapper;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", uses = {AddressMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    //UserDTO toDTO(User user);
    UserResponse toUserResponseDTO(User user);

    @Mapping(target = "createUserId", ignore = true)
    User toEntity(UserRequest userRequest);

    // For partial update
    @Mapping(target = "createUserId", ignore = true)
    void updateUserFromDto(UserRequest dto, @MappingTarget User entity);
}
