package com.umc.cmap.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagDto {
    private Long tagIdx;
    private String tagName;

    public TagDto(Long tagIdx, String tagName) {
        this.tagIdx = tagIdx;
        this.tagName = tagName;
    }
}
