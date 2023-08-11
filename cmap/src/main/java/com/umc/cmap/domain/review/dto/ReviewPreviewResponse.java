package com.umc.cmap.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewPreviewResponse {
    private ReviewWriterResponse userInfo;
    private String content;
    private String imageUrl;
    private Long imageCnt;
    private LocalDateTime createdAt;
}
