package com.umc.cmap.domain.review.service;

import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.entity.Content;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import com.umc.cmap.domain.review.repository.ReviewImageRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
    private CafeRepository cafeRepository;

    @Test
    void 모든_리뷰_찾기() {
        //given
        User user = User.builder()
                .name("name1")
                .email("email1")
                .password("password1")
                .nickname("nickname1")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        Cafe cafe = Cafe.builder()
                .name("cafe-name")
                .information("cafe-information")
                .build();
        cafeRepository.save(cafe);
        Review review = Review.builder()
                .user(user)
                .cafe(cafe)
                .content(new Content("comment-content"))
                .score(4.5)
                .build();
        reviewRepository.save(review);
        ReviewImage image1 = ReviewImage.builder()
                .imageUrl("review-image-url1.com")
                .review(review)
                .build();
        ReviewImage image2 = ReviewImage.builder()
                .imageUrl("review-image-url2.com")
                .review(review)
                .build();
        reviewImageRepository.save(image1);
        reviewImageRepository.save(image2);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<ReviewResponse> result = service.getAll(user.getIdx(), pageable);

        //then
        assertThat(result.get(0).getImages()).contains(image1, image2);

    }

}