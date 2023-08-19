package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.review.dto.ReviewPreviewResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ReviewPreviewService {

    ReviewService reviewService;
    ReviewRepository reviewRepository;
    ReviewImageRepository imageRepository;

    public List<ReviewPreviewResponse> get(Long cafeIdx, Pageable pageable) {
        List<Review> reviews = reviewRepository.findAllByCafeIdx(cafeIdx, pageable).stream().filter(r -> !r.getIsDeleted()).toList();
        log.info("[review] review-preview list of cafe(idx = " + cafeIdx + ") is selected");
        return reviews.stream().map(this::toResponse).toList();
    }

    private ReviewPreviewResponse toResponse(final Review review) {
        log.info("[review] call preview response");
        log.info("[review] review preview info: review idx: " + review.getIdx());
        return ReviewPreviewResponse.builder()
                .idx(review.getIdx())
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
        return image.map(ReviewImage::getImageUrl).orElse(null);
    }

    public Double getScoreAvg(final Long cafeIdx) {
        return reviewRepository.getScoreAvgByCafe(cafeIdx);
    }

    public Long getReviewCntByCafe(final Cafe cafe) {
        return reviewRepository.countByCafeAndIsDeletedFalse(cafe);
    }
}
