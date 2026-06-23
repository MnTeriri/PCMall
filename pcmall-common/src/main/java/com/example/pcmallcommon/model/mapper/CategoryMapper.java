package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Category;
import com.example.pcmallcommon.model.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toDto(CategoryEntity entity);

    List<Category> toDtoList(List<CategoryEntity> entities);

    CategoryEntity toEntity(Category dto);
}