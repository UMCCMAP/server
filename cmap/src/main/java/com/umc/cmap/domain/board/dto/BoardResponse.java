package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long idx;
    private String boardTitle;
    private String boardContent;
    private HashMap<Long, List<HashMap<Long,String>>> tagList;
    private List<TagDto> tagNames;
    private LocalDateTime createdAt;

    public BoardResponse(Long idx, String boardTitle, String boardContent, HashMap<Long, List<HashMap<Long,String>>> tagList, List<TagDto> tagNames, LocalDateTime createdAt) {
        this.idx = idx;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.tagList = tagList;
        this.tagNames = tagNames;
        this.createdAt = createdAt;
    }
}
