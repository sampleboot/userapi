package com.example.userapi.mapper;

import com.example.userapi.dto.AddressDTO;
import com.example.userapi.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {

    /**
     * Map a single AddressDTO to Address entity,
     * but do NOT map the 'user' back‑reference here.
     */
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO dto);

    /** Map a single Address entity to AddressDTO. */
    AddressDTO toDTO(Address entity);

    /**
     * Convert a list of DTOs to entities via the single‑element mapper.
     * Because this is a default method, MapStruct won’t try to interpret
     * @Mapping annotations on the List itself.
     */
    default List<Address> toEntities(List<AddressDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convert a list of entities to DTOs via the single‑element mapper.
     */
    default List<AddressDTO> toDTOs(List<Address> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
