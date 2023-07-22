package com.umc.cmap.domain.review.dto;

import com.umc.cmap.domain.review.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponse {
    private String userName;
    private String userImg;

    private String content;
    private Double score;
    private List<ReviewImage> reviewImages;
    private LocalDateTime createdAt;
}
