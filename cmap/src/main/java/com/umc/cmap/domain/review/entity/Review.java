package com.umc.cmap.domain.review.entity;

import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_idx")
    private Long idx;

    private String title;
    private String keyword;
    private String content;

    private Integer score;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;

    @Builder
    public Review(User user, Cafe cafe, String content, String title, String keyword, Integer score) {
        this.user = user;
        this.cafe = cafe;
        this.content = content;
        this.title = title;
        this.keyword = keyword;
        this.score = score;
        this.isDeleted = false;
    }

    public void update(ReviewRequest dto) {
        this.content = dto.getContent();
        this.title = dto.getTitle();
        this.keyword = dto.getKeyword();
        this.score = dto.getScore();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
