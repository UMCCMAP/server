package com.umc.cmap.domain.review.repository;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCafeIdx(Long cafeIdx, Pageable pageable);

    List<Review> findAllByUserIdx(Long userIdx, Pageable pageable);

    Long countByUserIdx(Long userIdx);

    List<Review> findByCafe(Cafe cafe);


    Long countByUserIdxAndIsDeletedFalse(Long userIdx);

    @Query("select avg(r.score) from Review r where r.cafe.idx = :cafeIdx and r.isDeleted = false")
    Double getScoreAvgByCafe(@Param("cafeIdx") Long cafeIdx);

    Long countByCafeAndIsDeletedFalse(Cafe cafe);

}
