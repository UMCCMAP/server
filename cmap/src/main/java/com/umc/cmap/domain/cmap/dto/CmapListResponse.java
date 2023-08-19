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
    private List<CmapCafeDto> cmapCafe;
    private List<HashMap<Long,String>> themeList;

    public CmapListResponse(List<CmapCafeDto> cmapCafe, List<HashMap<Long,String>> themeList) {
        this.cmapCafe = cmapCafe;
        this.themeList = themeList;
    }
}