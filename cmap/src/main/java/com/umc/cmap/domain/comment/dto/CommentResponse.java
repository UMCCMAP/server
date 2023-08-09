package com.umc.cmap.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    Long userIdx;
    String nickname;
    String userImg;

    Long commentIdx;
    String content;
    LocalDateTime createdAt;
}
