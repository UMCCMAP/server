package com.umc.cmap.domain.review.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseTimeEntity {

    private String content;

    @Builder
    public Content(String content) {
        this.content = content;
    }

}
