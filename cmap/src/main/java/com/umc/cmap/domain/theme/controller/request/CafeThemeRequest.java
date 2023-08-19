package com.umc.cmap.domain.theme.controller.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CafeThemeRequest {
    private String themeName;
    private Long cafeIdx; //cafe_idx
    private Long themeIdx;

}