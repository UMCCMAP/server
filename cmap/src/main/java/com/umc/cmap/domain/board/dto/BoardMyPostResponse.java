package com.umc.cmap.domain.board.dto;

import com.umc.cmap.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardMyPostResponse {
    private Long idx;
    private String cafeName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickName;

    public BoardMyPostResponse(Board board) {
        this.idx = board.getIdx();
        this.cafeName = board.getCafe().getName();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.nickName = board.getUser().getNickname();
    }
}
