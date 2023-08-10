package com.umc.cmap.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponse {
    private ReviewWriterResponse userInfo;
    private String content;
    private Double score;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
