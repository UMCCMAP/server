package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardListResponse {
    private Page<BoardResponse> boardResponses;
    private List<TagDto> tagNames;

    public BoardListResponse(Page<BoardResponse> boardResponses, List<TagDto> tagNames) {
        this.boardResponses = boardResponses;
        this.tagNames = tagNames;
    }
}
