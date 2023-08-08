package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService service;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CafeRepository cafeRepository;

    @Test
    void 모든_리뷰_찾기() {
        //given
        User user = User.builder()
                .name("name1")
                .email("email1")
                .password("password1")
                .nickname("nickname1")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Profile profile = Profile.builder()
                .userImg("https://user-profile.com")
                .user(user)
                .build();
        profileRepository.save(profile);

        Cafe cafe = Cafe.builder()
                .name("cafe-name")
                .info("cafe-information")
                .build();
        cafeRepository.save(cafe);

        Review review = Review.builder()
                .user(user)
                .cafe(cafe)
                .content("comment-content")
                .score(4.5)
                .build();
        reviewRepository.save(review);

        ReviewImage image1 = ReviewImage.builder()
                .imageUrl("review-image-url1.com")
                .review(review)
                .build();
        reviewImageRepository.save(image1);
        ReviewImage image2 = ReviewImage.builder()
                .imageUrl("review-image-url2.com")
                .review(review)
                .build();

        reviewImageRepository.save(image2);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<ReviewResponse> result = service.getAll(user.getIdx(), pageable);

        //then
        assertThat(result.get(0).getImageUrls()).contains(image1.getImageUrl(), image2.getImageUrl());

    }

    @Test
    void 리뷰_저장() {
        Cafe cafe = Cafe.builder()
                .name("cafe-name")
                .info("cafe-information")
                .build();
        cafeRepository.save(cafe);
        String content = "this is content";
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://image-url/1");
        Double score = 4.5;

        ReviewRequest request = ReviewRequest.builder()
                .score(score)
                .content(content)
                .imageUrls(imageUrls)
                .build();

        service.save(cafe.getIdx(), request);

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews.stream().anyMatch(r -> r.getContent().equals(content))).isTrue();
    }


    @Test
    void 사용자별_리뷰_수() {
        //given
        User user = User.builder()
                .name("name1")
                .email("email1")
                .password("password1")
                .nickname("nickname1")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Cafe cafe = Cafe.builder()
                .name("cafe-name")
                .info("cafe-information")
                .build();
        cafeRepository.save(cafe);

        Review review = Review.builder()
                .user(user)
                .cafe(cafe)
                .content("review-content")
                .build();
        reviewRepository.save(review);

        //when
        Long result = service.getUserReviewsCnt(user.getIdx());

        //then
        assertThat(result).isEqualTo(1L);
    }

}