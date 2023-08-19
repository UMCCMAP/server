package com.umc.cmap.domain.cmap.dto;

import lombok.Data;

import java.util.List;

@Data
public class CmapSearchResponse {

    private Long cafeIdx;
    private String cafeName;
    private List<String> cafeThemes;
    private String cafeImage;
    private Integer cafeReviewCount;
    private Integer cafePostCount;
    private Double cafeAverageScore;

    public CmapSearchResponse(Long cafeIdx, String cafeName, List<String> cafeThemes, String cafeImage,
                              Integer cafeReviewCount, Integer cafePostCount, Double cafeAverageScore) {
        this.cafeIdx = cafeIdx;
        this.cafeName = cafeName;
        this.cafeThemes = cafeThemes;
        this.cafeImage = cafeImage;
        this.cafeReviewCount = cafeReviewCount;
        this.cafePostCount = cafePostCount;
        this.cafeAverageScore = cafeAverageScore;
    }
}
