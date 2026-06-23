package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Order;
import com.example.pcmallcommon.model.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toDto(OrderEntity entity);

    List<Order> toDtoList(List<OrderEntity> entities);

    OrderEntity toEntity(Order dto);
}