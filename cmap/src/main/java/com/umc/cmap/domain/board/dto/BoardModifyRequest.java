package com.umc.cmap.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardModifyRequest {
    private Long userIdx;
    private Long cafeIdx;
    private String boardTitle;
    private String boardContent;
}
