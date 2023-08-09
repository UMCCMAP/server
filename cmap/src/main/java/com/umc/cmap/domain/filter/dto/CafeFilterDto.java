package com.umc.cmap.domain.filter.dto;

import lombok.Builder;


@Builder
public class CafeFilterDto {
    private Long idx;
    private String name;
    private String theme;
    private Long cafeIdx;
}
