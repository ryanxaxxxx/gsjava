package com.greenway.greenway.mapper;

import com.greenway.greenway.dto.UserDTO;
import com.greenway.greenway.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // DTO → Entity (criação)
    @Mapping(target = "password", ignore = true)  // senha é tratada no Service
    @Mapping(target = "role", ignore = true)      // role será definida no Service (ex: USER)
    @Mapping(target = "companyEmail", ignore = true)
    User toEntity(UserDTO dto);

    // Entity → DTO
    @Mapping(target = "id", source = "id")
    UserDTO toDto(User user);

    // Atualização parcial (PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "companyEmail", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}
