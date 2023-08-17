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
    private List<CmapCafeDto> cmapCafeDtos;
    private List<HashMap<Long,String>> themeList;

    public CmapListResponse(List<CmapCafeDto> cmapCafeDtos, List<HashMap<Long,String>> themeList) {
        this.cmapCafeDtos = cmapCafeDtos;
        this.themeList = themeList;
    }
}