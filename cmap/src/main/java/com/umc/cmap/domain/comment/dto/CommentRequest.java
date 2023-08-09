package com.umc.cmap.domain.comment.dto;

import com.umc.cmap.domain.board.entity.Board;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    private Board board;
    @NotNull private String content;
}
