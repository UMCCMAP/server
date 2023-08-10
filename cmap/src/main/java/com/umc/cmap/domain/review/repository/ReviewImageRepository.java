package com.umc.cmap.domain.review.repository;

import com.umc.cmap.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findAllByReviewIdx(Long reviewIdx);

    Long countByReviewIdx(Long reviewIdx);

    Optional<ReviewImage> findFirstByReviewIdx(Long reviewIdx);
}
