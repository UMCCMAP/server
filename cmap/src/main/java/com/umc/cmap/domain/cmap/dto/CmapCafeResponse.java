package com.umc.cmap.domain.cmap.dto;

import com.umc.cmap.domain.cmap.entity.Type;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmapCafeResponse {
    private Double scoreAvg;
    private Long boardCnt;
    private Long reviewCnt;
    private Type type;
}
