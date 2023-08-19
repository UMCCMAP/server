package com.umc.cmap.domain.cmap.dto;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cmap.entity.Type;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CmapStateRequest {
    private Cafe cafe;
    @NotNull private Type type;
}
