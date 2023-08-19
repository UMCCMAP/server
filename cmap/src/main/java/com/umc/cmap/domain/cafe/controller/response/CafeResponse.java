package com.umc.cmap.domain.cafe.controller.response;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.dto.ReviewWriterResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.theme.controller.response.CafeThemeResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CafeResponse {

    private Long idx;
    private String name;
    private String city;
    private String district;
    private String info;
    private List<CafeThemeResponse> cafeThemes;
    private String image;
    private Double cafeLatitude;
    private Double cafeLongitude;
    private List<ReviewResponse> reviews;
    private Double averageRating;
    private Integer totalPosts;
    private Integer totalReviews;

    public CafeResponse(Cafe cafe, List<Review> reviews) {
        this.idx = cafe.getIdx();
        this.name = cafe.getName();
        this.city = cafe.getCity();
        this.district = cafe.getDistrict();
        this.cafeThemes = cafe.getCafeThemes().stream()
                .map(CafeThemeResponse::new)
                .collect(Collectors.toList());
        this.reviews = reviews.stream()
                .filter(review -> !review.getIsDeleted())
                .map(review -> new ReviewResponse(
                        new ReviewWriterResponse(),
                        review.getIdx(),
                        review.getContent(),
                        review.getScore(),
                        review.getTitle(),
                        review.getKeyword(),
                        null,
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());

        this.image = cafe.getImage();
        this.cafeLatitude = cafe.getLocation().getLatitude();
        this.cafeLongitude = cafe.getLocation().getLongitude();
        this.averageRating = calculateAverageRating(reviews);
        this.totalPosts = calculateTotalPosts(reviews);
        this.totalReviews = reviews.size();


    }


    private Double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double totalScore = 0.0;
        for (Review review : reviews) {
            totalScore += review.getScore();
        }
        return totalScore / reviews.size();
    }

    private Integer calculateTotalPosts(List<Review> reviews) {
        int totalPosts = 0;
        for (Review review : reviews) {
            if (!review.getIsDeleted()) {
                totalPosts++;
            }
        }
        return totalPosts;
    }


}

