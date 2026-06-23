package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.User;
import com.example.pcmallcommon.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toDto(UserEntity entity);

    List<User> toDtoList(List<UserEntity> entities);

    UserEntity toEntity(User dto);
}
