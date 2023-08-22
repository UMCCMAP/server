package com.umc.cmap.domain.cmap.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CmapCafeDto {
    private Long cafeIdx;
    private String cafeName;
    private String cafeImg;

    public CmapCafeDto(Long cafeIdx, String cafeName, String cafeImg) {
        this.cafeIdx = cafeIdx;
        this.cafeName = cafeName;
        this.cafeImg = cafeImg;
    }
}