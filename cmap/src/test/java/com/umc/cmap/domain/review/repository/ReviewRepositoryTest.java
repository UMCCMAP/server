package com.umc.cmap.domain.review.repository;

import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.review.entity.Content;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Transactional
@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository repository;
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
                .info("cafe-information")
                .build();
        cafeRepository.save(cafe);
        Review review = Review.builder()
                .user(user)
                .cafe(cafe)
                .content(new Content("comment-content"))
                .score(4.5)
                .build();

        Pageable pageable = PageRequest.of(0, 5);

        //when
        repository.save(review);
        List<Review> data = repository.findAllByCafeIdx(cafe.getIdx(), pageable);

        //then
        Assertions.assertThat(data.get(0).getUser()).isEqualTo(user);
    }
}