package com.umc.cmap.domain.cmap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CmapCafeDto {

    private Long userId;
    private Long cafeIdx;
    private String type;
    private String cafeTitle;

    private Long idx;
    private String cafeCity;
    private String cafeDistrict;
    private Double cafeLatitude;
    private Double cafeLongitude;

    public CmapCafeDto(Long idx,Long userId, Long cafeIdx, String type, String cafeTitle,
                       String cafeCity, String cafeDistrict,
                       Double cafeLatitude, Double cafeLongitude) {
        this.idx=idx;
        this.userId = userId;
        this.cafeIdx = cafeIdx;
        this.type = type;
        this.cafeTitle = cafeTitle;
        this.cafeCity = cafeCity;
        this.cafeDistrict = cafeDistrict;
        this.cafeLatitude = cafeLatitude;
        this.cafeLongitude = cafeLongitude;
    }
}