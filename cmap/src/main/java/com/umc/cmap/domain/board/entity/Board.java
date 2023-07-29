package com.umc.cmap.domain.board.entity;

import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cafe_idx")
    private Cafe cafe;

    private String boardTitle;

    private String boardContent;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Timestamp removedAt;

    @Builder
    public Board(User user, Cafe cafe, String boardTitle, String boardContent, Role role) {
        this.user = user;
        this.cafe = cafe;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.role = role;
    }

    public void removeBoard(){
        this.removedAt = new Timestamp(System.currentTimeMillis());
    }

    public Board modifyPost(Cafe cafe, String boardTitle, String boardContent) {
        return new Board(this.user, cafe, boardTitle, boardContent, this.role);
    }

}