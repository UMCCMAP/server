package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findBoardImageListByBoardIdx(Long boardIdx);

    Optional<BoardImage> findTop1ByBoard(Board board);
}
