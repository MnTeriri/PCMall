package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Storage;
import com.example.pcmallcommon.model.entity.StorageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StorageMapper {
    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    Storage toDto(StorageEntity entity);

    List<Storage> toDtoList(List<StorageEntity> entities);

    StorageEntity toEntity(Storage dto);
}
