package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.model.entity.GoodsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoodsMapper {

    GoodsMapper INSTANCE = Mappers.getMapper(GoodsMapper.class);

    Goods toDto(GoodsEntity entity);

    List<Goods> toDtoList(List<GoodsEntity> entities);

    GoodsEntity toEntity(Goods dto);
}