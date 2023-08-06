package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.mapper.ReviewImageMapper;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageMapper imageMapper;
    private final ReviewImageRepository reviewImageRepository;


    @Transactional
    public void saveAll(List<String> urls, Review review) {
        urls.forEach(u -> reviewImageRepository.save(imageMapper.toEntity(u, review)));
    }

    public List<String> getAll(Long reviewIdx) {
        return reviewImageRepository.findAllByReviewIdx(reviewIdx).stream().map(ReviewImage::getImageUrl).toList();
    }

    @Transactional
    public void deleteAll(Review review) {
        reviewImageRepository.deleteAll(reviewImageRepository.findAllByReviewIdx(review.getIdx()));
    }
}
