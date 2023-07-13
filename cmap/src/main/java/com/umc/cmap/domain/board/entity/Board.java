package com.umc.cmap.domain.board.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Setter;

public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @ManyToOne
    @JoinColumn(name="userIdx")
    private User user;

    @ManyToOne
    @JoinColumn(name="cafeIdx")
    private Cafe cafe;

    private String boardTitle;

    private String boardContent;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @Builder
    public Board(User user, Cafe cafe, String boardTitle, String boardContent, Role role) {
        this.user = user;
        this.cafe = cafe;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.role = role;
    }
}
