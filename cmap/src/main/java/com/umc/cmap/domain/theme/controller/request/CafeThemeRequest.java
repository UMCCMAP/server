package com.umc.cmap.domain.theme.controller.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeThemeRequest {
    private String themeName;
    private Long cafeIdx; //cafe_idx

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Long getCafeIdx() {
        return cafeIdx;
    }

    public void setCafeIdx(Long cafeIdx) {
        this.cafeIdx = cafeIdx;
    }
}