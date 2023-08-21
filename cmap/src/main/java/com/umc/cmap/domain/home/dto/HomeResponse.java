package com.umc.cmap.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HomeResponse {
    private Long boardIdx;
    private String imageUrl;
}
