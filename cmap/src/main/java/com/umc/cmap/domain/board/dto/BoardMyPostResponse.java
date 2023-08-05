package com.umc.cmap.domain.board.dto;

import com.umc.cmap.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardMyPostResponse {
    private Long idx;
    private String cafeName;
    private String name;
    private String boardTitle;
    private String boardContent;
    private HashMap<Long, List<HashMap<Long, String>>> tagList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardMyPostResponse(Board board, HashMap<Long, List<HashMap<Long, String>>> tagList) {
        this.idx = board.getIdx();
        this.cafeName = board.getCafe().getName();
        this.name = board.getUser().getName();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.tagList = tagList;
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }
}
