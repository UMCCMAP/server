package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long idx;
    private String boardTitle;
    private String boardContent;
    private List<Map<Long,String>> tagNames;
    private LocalDateTime createdAt;

    public BoardResponse(Long idx, String boardTitle, String boardContent, List<Map<Long,String>> tagNames, LocalDateTime createdAt) {
        this.idx = idx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.tagNames = tagNames;
        this.createdAt = createdAt;
    }
}
