package com.umc.cmap.domain.cafe.controller.response;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.theme.controller.response.CafeThemeResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CafeResponse {
    private Long idx;
    private String name;
    private String city;
    private String district;
    private String info;
    private List<CafeThemeResponse> cafeThemes;

    public CafeResponse(Cafe cafe) {
        this.idx = cafe.getIdx();
        this.name = cafe.getName();
        this.city = cafe.getCity();
        this.district = cafe.getDistrict();
        this.info = cafe.getInfo();
        if (cafe.getCafeThemes() != null && !cafe.getCafeThemes().isEmpty()) {
            this.cafeThemes = cafe.getCafeThemes().stream()
                    .map(CafeThemeResponse::new)
                    .collect(Collectors.toList());
        } else {
            this.cafeThemes = new ArrayList<>();
        }
    }


}