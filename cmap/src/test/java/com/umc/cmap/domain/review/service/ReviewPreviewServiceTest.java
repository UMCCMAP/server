package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.dto.ReviewPreviewResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewPreviewServiceTest {
    @Autowired private ReviewPreviewService previewService;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewImageRepository reviewImageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private CafeRepository cafeRepository;

//    @Retention(RetentionPolicy.RUNTIME)
//    @WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
//    public @interface WithMockCustomUser {
//
//        String username() default "foo";
//
//        String grade() default "ADMIN";
//
//    }

//    @WithMockUser
    @Test
    void 카페별_리뷰_미리보기() {
        //given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setParameter("firstName", "Spring");
//        request.setParameter("lastName", "Test");
//        MockHttpServletResponse response = new MockHttpServletResponse();


        User user = User.builder()
                .name("name1")
                .email("email1")
                .password("password1")
                .nickname("nickname1")
                .role(Role.USER)
                .build();
        userRepository.save(user);
//        User user = userRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

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

        Review review1 = Review.builder()
                .user(user)
                .cafe(cafe)
                .content("comment-content")
                .score(4)
                .build();
        reviewRepository.save(review1);

        ReviewImage image1 = ReviewImage.builder()
                .imageUrl("review-image-url1.com")
                .review(review1)
                .build();
        reviewImageRepository.save(image1);

        ReviewImage image2 = ReviewImage.builder()
                .imageUrl("review-image-url2.com")
                .review(review1)
                .build();
        reviewImageRepository.save(image2);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<ReviewPreviewResponse> result = previewService.get(cafe.getIdx(), pageable);

        //then
        assertThat(result.get(0).getImageCnt().longValue()).isEqualTo(2L);
    }

    @Test
    void 카페별_이미지_없는_리뷰_미리보기() {
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

        Review review1 = Review.builder()
                .user(user)
                .cafe(cafe)
                .content("comment-content")
                .score(4)
                .build();
        reviewRepository.save(review1);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<ReviewPreviewResponse> result = previewService.get(cafe.getIdx(), pageable);

        //then
        assertThat(result.get(0).getImageCnt().longValue()).isZero();
    }

}