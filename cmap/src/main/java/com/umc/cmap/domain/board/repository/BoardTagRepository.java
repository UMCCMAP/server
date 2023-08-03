package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    List<BoardTag> findTagIdxListByBoardIdx(Long boardIdx);
    @Query("SELECT DISTINCT boardTag.board FROM BoardTag boardTag WHERE boardTag.tag.idx IN :tagIdxList")
    List<Board> findBoardByTagIn(@Param("tagIdxList") List<Long> tagIdxList);
    @Query("SELECT boardTag.tag.idx FROM BoardTag boardTag WHERE boardTag.board.idx = :boardIdx")
    List<Long> findTagIdxByBoardIdx(@Param("boardIdx") Long boardIdx);
}
