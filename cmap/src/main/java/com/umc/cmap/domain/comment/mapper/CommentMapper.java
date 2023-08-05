package com.umc.cmap.domain.comment.mapper;

import com.umc.cmap.domain.comment.dto.CommentRequest;
import com.umc.cmap.domain.comment.dto.CommentResponse;
import com.umc.cmap.domain.comment.entity.Comment;
import com.umc.cmap.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "userIdx", source = "user.idx")
    @Mapping(target = "commentIdx", source = "comment.idx")
    @Mapping(target = "createdAt", source = "comment.createdAt")
    CommentResponse toResponse(Comment comment, User user);

    Comment toEntity(CommentRequest dto, User user);

}
