package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long idx;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;

    public BoardResponse(Long idx, String boardTitle, String boardContent, LocalDateTime createdAt) {
        this.idx = idx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.createdAt = createdAt;
    }
}
