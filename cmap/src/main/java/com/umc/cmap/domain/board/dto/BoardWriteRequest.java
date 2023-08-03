package com.umc.cmap.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteRequest {
    private Long userIdx;
    private Long cafeIdx;
    private String boardTitle;
    private String boardContent;
}
