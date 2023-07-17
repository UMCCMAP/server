package com.umc.cmap.domain.comment.entity;

import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_idx")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    private User user;

    private String comment_content;

    private Timestamp removedAt;

    @Builder
    public Comment(User user, Board board, String comment_content) {
        this.board = board;
        this.user = user;
        this.comment_content = comment_content;
    }

    public void removeComment(){
        this.removedAt = new Timestamp(System.currentTimeMillis());
    }


}