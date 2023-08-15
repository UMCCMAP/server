package com.umc.cmap.domain.cmap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CmapListResponse {
    private String cafeTitle;
    List<HashMap<Long,String>> themeList;

    public CmapListResponse(String cafeTitle) {
        this.cafeTitle = cafeTitle;
    }
}
