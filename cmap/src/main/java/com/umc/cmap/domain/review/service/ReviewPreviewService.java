package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.review.dto.ReviewPreviewResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ReviewPreviewService {

    ReviewService reviewService;
    ReviewRepository reviewRepository;
    ReviewImageRepository imageRepository;

    public List<ReviewPreviewResponse> get(Long cafeIdx, Pageable pageable) {
        List<Review> reviews = reviewRepository.findAllByCafeIdx(cafeIdx, pageable);
        return reviews.stream().map(this::toResponse).toList();
    }

    private ReviewPreviewResponse toResponse(final Review review) {
        return ReviewPreviewResponse.builder()
                .userInfo(reviewService.getWriter(review.getUser()))
                .content(review.getContent())
                .imageUrl(getOneImageUrl(review.getIdx()))
                .imageCnt(getImageCnt(review.getIdx()))
                .createdAt(review.getCreatedAt())
                .build();
    }

    private Long getImageCnt(Long reviewIdx) {
        return imageRepository.countByReviewIdx(reviewIdx);
    }

    private String getOneImageUrl(Long reviewIdx) {
        Optional<ReviewImage> image = imageRepository.findFirstByReviewIdx(reviewIdx);
        String imageUrl = "";
        if (image.isPresent()) {
            imageUrl = image.get().getImageUrl();
        }
        return imageUrl;
    }
}
