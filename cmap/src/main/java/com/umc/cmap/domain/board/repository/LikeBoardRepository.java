package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    @Query("SELECT likeBoard FROM LikeBoard likeBoard WHERE likeBoard.board.idx = :boardIdx AND likeBoard.user.idx = :userIdx")
    LikeBoard findByBoardIdxAndUserIdx(@Param("boardIdx") Long boardIdx, @Param("userIdx") Long userIdx);
}
