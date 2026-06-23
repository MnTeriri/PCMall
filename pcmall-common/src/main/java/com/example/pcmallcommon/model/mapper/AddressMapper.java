package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Address;
import com.example.pcmallcommon.model.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toDto(AddressEntity entity);

    List<Address> toDtoList(List<AddressEntity> entities);

    AddressEntity toEntity(Address dto);
}
