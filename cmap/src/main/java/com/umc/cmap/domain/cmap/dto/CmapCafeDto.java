package com.umc.cmap.domain.cmap.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CmapCafeDto {
    private Long cafeIdx;
    private String cafeTitle;

    public CmapCafeDto(Long cafeIdx, String cafeTitle) {
        this.cafeIdx = cafeIdx;
        this.cafeTitle = cafeTitle;
    }
}