package com.example.userapi.mapper;

import com.example.userapi.dto.UserDTO;
import com.example.userapi.entity.User;
import org.mapstruct.*;



@Mapper(componentModel = "spring", uses = { AddressMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    // For partial update
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
}
