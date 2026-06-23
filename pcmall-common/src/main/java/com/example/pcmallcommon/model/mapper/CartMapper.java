package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Cart;
import com.example.pcmallcommon.model.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    Cart toDto(CartEntity entity);

    List<Cart> toDtoList(List<CartEntity> entities);

    CartEntity toEntity(Cart dto);
}