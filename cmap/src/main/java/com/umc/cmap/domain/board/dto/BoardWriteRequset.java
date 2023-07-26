package com.umc.cmap.domain.board.dto;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteRequset {
    private Long userIdx;
    private Long cafeIdx;
    private String boardTitle;
    private String boardContent;
}
