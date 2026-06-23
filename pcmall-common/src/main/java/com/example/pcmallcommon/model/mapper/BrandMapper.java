package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Brand;
import com.example.pcmallcommon.model.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand toDto(BrandEntity entity);

    List<Brand> toDtoList(List<BrandEntity> entities);

    BrandEntity toEntity(Brand dto);
}
