package com.umc.cmap.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteRequset {
    private String boardTitle;
    private String boardContent;
    private Long cafeIdx;
}
