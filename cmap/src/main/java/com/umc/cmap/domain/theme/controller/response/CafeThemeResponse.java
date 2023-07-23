package com.umc.cmap.domain.theme.controller.response;

import com.umc.cmap.domain.theme.entity.CafeTheme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CafeThemeResponse {
    private Long idx;
    private String themeName;
    private Long cafeIdx;

    public CafeThemeResponse(CafeTheme cafeTheme) {
        this.idx = cafeTheme.getIdx();
        this.themeName = cafeTheme.getTheme().getName();
        this.cafeIdx = cafeTheme.getCafe().getIdx();
    }
}

