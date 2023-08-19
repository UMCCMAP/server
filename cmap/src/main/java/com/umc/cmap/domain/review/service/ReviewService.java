package com.umc.cmap.domain.review.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.dto.ReviewWriterResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.mapper.ReviewMapper;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umc.cmap.config.BaseResponseStatus.DONT_HAVE_ACCESS;

@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ReviewService {

    ReviewImageService imageService;
    ReviewRepository reviewRepository;
    CafeRepository cafeRepository;
    AuthService authService;
    ProfileRepository profileRepository;
    ReviewMapper mapper;


    public List<ReviewResponse> getAll(Long id, Pageable pageable) {
        List<Review> reviews = reviewRepository.findAllByCafeIdx(id, pageable).stream().filter(r -> !r.getIsDeleted()).toList();
        return reviews.stream().map(r -> mapper.toResponse(r, imageService.getAll(r.getIdx()), getWriter(r.getUser()))).toList();
    }

    public ReviewResponse getOne(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toResponse(review, imageService.getAll(id), getWriter(review.getUser()));
    }

    public ReviewWriterResponse getWriter(User user) {
        return ReviewWriterResponse.builder()
                .userIdx(user.getIdx())
                .userNickname(user.getNickname())
                .userImg(getWriterProfileImg(user.getIdx()))
                .build();
    }

    private String getWriterProfileImg(Long userIdx) {
        Profile profile = profileRepository.findByUserIdx(userIdx).orElseThrow(EntityNotFoundException::new);
        return profile.getUserImg();
    }

    public List<ReviewResponse> getAllUserReviews(Long userIdx, Pageable pageable) {
        List<Review> reviews = reviewRepository.findAllByUserIdx(userIdx, pageable).stream().filter(r -> !r.getIsDeleted()).toList();
        return reviews.stream().map(r -> mapper.toResponse(r, imageService.getAll(r.getIdx()), getWriter(r.getUser()))).toList();
    }

    public Long getUserReviewsCnt(Long userIdx) {
        return reviewRepository.countByUserIdx(userIdx);
    }

    @Transactional
    public void save(Long cafeIdx, ReviewRequest param, HttpServletRequest request) throws BaseException {
        param.setCafe(getCafeEntity(cafeIdx));
        Review review = reviewRepository.save(mapper.toEntity(param, authService.getUser(request)));
        imageService.saveAll(param.getImageUrls(), review);
    }

    private Cafe getCafeEntity(Long idx) {
        return cafeRepository.findById(idx).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long reviewIdx, ReviewRequest param, HttpServletRequest request) throws BaseException {
        Review review = reviewRepository.findById(reviewIdx).orElseThrow(EntityNotFoundException::new);
        if (!isValidRequest(review.getUser(), request)) {
            throw new BaseException(DONT_HAVE_ACCESS);
        }

        review.update(param);
        updateReviewImages(param.getImageUrls(), review);
    }

    private boolean isValidRequest(User writer, HttpServletRequest request) throws BaseException {
        return writer.equals(authService.getUser(request));
    }

    private void updateReviewImages(List<String> urls, Review review) {
        imageService.deleteAll(review);
        imageService.saveAll(urls, review);
    }

    @Transactional
    public void delete(Long param, HttpServletRequest request) throws BaseException {
        Review review = reviewRepository.findById(param).orElseThrow(EntityNotFoundException::new);
        if (!isValidRequest(review.getUser(), request)) {
            throw new BaseException(DONT_HAVE_ACCESS);
        }
        review.delete();
    }

    public List<Review> getCafeReviews(Cafe cafe) {
        return reviewRepository.findByCafe(cafe);
    }

}