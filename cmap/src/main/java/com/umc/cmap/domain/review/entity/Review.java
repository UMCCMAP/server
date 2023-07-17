package com.umc.cmap.domain.review.entity;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_idx")
    private Long idx;

    @Embedded
    private Content content;

    private Double score;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;

    @Builder
    public Review(User user, Cafe cafe, Content content, Double score) {
        this.user = user;
        this.cafe = cafe;
        this.content = content;
        this.score = score;
        this.isDeleted = false;
    }
}
