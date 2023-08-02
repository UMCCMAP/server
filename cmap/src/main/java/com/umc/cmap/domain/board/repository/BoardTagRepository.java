package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    List<Long> findTagIdxListByBoardIdx(Long boardIdx);
}
