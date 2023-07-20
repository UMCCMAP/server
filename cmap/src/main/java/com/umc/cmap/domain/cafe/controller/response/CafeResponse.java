package com.umc.cmap.domain.cafe.controller.response;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.theme.controller.response.CafeThemeResponse;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CafeResponse {
    private Long idx;
    private String name;
    private String location;
    private String info;
    private CafeThemeResponse cafeTheme;

    public CafeResponse(Cafe cafe) {
        this.idx = cafe.getIdx();
        this.name = cafe.getName();
        this.location = cafe.getLocation();
        this.info = cafe.getInfo();
        this.cafeTheme = cafe.getCafeTheme() != null ? new CafeThemeResponse(cafe.getCafeTheme()) : null;
    }


}