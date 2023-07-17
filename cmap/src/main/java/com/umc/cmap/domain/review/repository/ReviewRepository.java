package com.umc.cmap.domain.review.repository;

import com.umc.cmap.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCafeIdx(Long cafeIdx, Pageable pageable);
}
