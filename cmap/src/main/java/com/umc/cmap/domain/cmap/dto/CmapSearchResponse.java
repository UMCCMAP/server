package com.umc.cmap.domain.cmap.dto;

import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.theme.controller.response.CafeThemeResponse;
import lombok.Data;

import java.util.List;

@Data
public class CmapSearchResponse {

    private Long idx;
    private String name;
    private String city;
    private String district;
    private String info;
    private List<CafeThemeResponse> cafeThemes;
    private String image;
    private Double cafeLatitude;
    private Double cafeLongitude;
    private Double averageRating;
    private Integer totalPosts;
    private Integer totalReviews;
    private Type type;


    public CmapSearchResponse(CafeResponse cafeResponse, Type type) {
        this.idx = cafeResponse.getIdx();
        this.name = cafeResponse.getName();
        this.city = cafeResponse.getCity();
        this.district = cafeResponse.getDistrict();
        this.info = cafeResponse.getInfo();
        this.cafeThemes = cafeResponse.getCafeThemes();
        this.image = cafeResponse.getImage();
        this.cafeLatitude = cafeResponse.getCafeLatitude();
        this.cafeLongitude = cafeResponse.getCafeLongitude();
        this.averageRating = cafeResponse.getAverageRating();
        this.totalPosts = cafeResponse.getTotalPosts();
        this.totalReviews = cafeResponse.getTotalReviews();
        this.type = type;
    }
}

