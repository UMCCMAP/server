package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.mapper.ReviewMapper;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewMapper mapper;


    public List<ReviewResponse> getAll(Long id, Pageable pageable) {
        return reviewRepository.findAllByCafeIdx(id, pageable).stream().filter(r -> !r.getIsDeleted())
                .map(r -> mapper.toResponse(r, reviewImageRepository.findAllByReviewIdx(r.getIdx()))).toList();
    }
}
