package com.umc.cmap.domain.review.dto;

import com.umc.cmap.domain.review.entity.Content;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponse {
    private User user;
    private Content content;
    private Double score;
    private List<ReviewImage> images;
}
