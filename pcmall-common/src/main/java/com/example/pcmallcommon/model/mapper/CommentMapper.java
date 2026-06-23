package com.example.pcmallcommon.model.mapper;

import com.example.pcmallcommon.model.dto.Comment;
import com.example.pcmallcommon.model.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment toDto(CommentEntity entity);

    List<Comment> toDtoList(List<CommentEntity> entities);

    CommentEntity toEntity(Comment dto);
}
