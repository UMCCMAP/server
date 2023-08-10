package com.umc.cmap.domain.board.entity;

import com.umc.cmap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_board_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "board_idx")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Builder
    public LikeBoard(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
