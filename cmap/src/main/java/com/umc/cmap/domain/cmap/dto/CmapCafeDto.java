package com.umc.cmap.domain.cmap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CmapCafeDto {
    private Long cafeIdx;
    private String cafeImg;

    public CmapCafeDto(Long cafeIdx, String cafeImg) {
        this.cafeIdx = cafeIdx;
        this.cafeImg = cafeImg;
    }
}