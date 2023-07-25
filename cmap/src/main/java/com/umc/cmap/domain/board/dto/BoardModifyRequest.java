package com.umc.cmap.domain.board.dto;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardModifyRequest {
    private User user;
    private Cafe cafe;
    private String boardTitle;
    private String boardContent;
}
