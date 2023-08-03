package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.mapper.ReviewImageMapper;
import com.umc.cmap.domain.review.mapper.ReviewMapper;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final CafeRepository cafeRepository;
    private final ReviewMapper mapper;
    private final ReviewImageMapper imageMapper;


    public List<ReviewResponse> getAll(Long id, Pageable pageable) {
        return reviewRepository.findAllByCafeIdx(id, pageable).stream().filter(r -> !r.getIsDeleted())
                .map(r -> mapper.toResponse(r, reviewImageRepository.findAllByReviewIdx(r.getIdx()))).toList();
    }

    public ReviewResponse getOne(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toResponse(review, reviewImageRepository.findAllByReviewIdx(id));
    }

    @Transactional
    public void save(Long cafeIdx, ReviewRequest param) {
        param.setCafe(getCafeEntity(cafeIdx));
        Review review = reviewRepository.save(mapper.toEntity(param));  //유저 정보 추가해야 함
        saveImages(param.getImageUrls(), review);
    }

    @Transactional
    public void saveImages(List<String> urls, Review review) {
        reviewImageRepository.save(imageMapper.toEntity(urls, review));
    }

    private Cafe getCafeEntity(Long idx) {
        return cafeRepository.findById(idx).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long reviewIdx, ReviewRequest param) {
        Review review = reviewRepository.findById(reviewIdx).orElseThrow(EntityNotFoundException::new);
        review.update(param.getContent(), param.getScore());
        updateReviewImages(param.getImageUrls(), review);
    }

    @Transactional
    public void updateReviewImages(List<String> urls, Review review) {
        reviewImageRepository.deleteAll(reviewImageRepository.findAllByReviewIdx(review.getIdx()));
        saveImages(urls, review);
    }


}
