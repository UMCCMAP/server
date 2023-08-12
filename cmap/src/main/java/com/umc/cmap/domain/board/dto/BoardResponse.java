package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long idx;
    private String boardTitle;
    private String boardContent;
    private HashMap<Long, List<HashMap<Long, String>>> tagList;
    private List<String> imgList;
    private LocalDateTime createdAt;

    public BoardResponse(Long idx, String boardTitle, String boardContent, HashMap<Long, List<HashMap<Long, String>>> tagList, List<String> imgList, LocalDateTime createdAt) {
        this.idx = idx;
        this.boardTitle = generatePreview(boardTitle, 10);
        this.boardContent = generatePreview(boardContent, 20);
        this.tagList = tagList;
        this.imgList = imgList;
        this.createdAt = createdAt;
    }

    private String generatePreview(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + " ..";
        }
        return text;
    }
}
