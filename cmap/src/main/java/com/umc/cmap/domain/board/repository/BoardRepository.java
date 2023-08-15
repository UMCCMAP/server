package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByRemovedAtIsNull(Pageable pageable);
    Page<Board> findByUserAndRemovedAtIsNull(User user, Pageable pageable);
    Page<Board> findByIdxInAndRemovedAtIsNull(List<Long> boardIdx, Pageable pageable);
    Page<Board> findByCafeIdxAndRemovedAtIsNull(Long cafeIdx);
    Page<Board> findByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(String boardTitle, String boardContent, Pageable pageable);
    Long countByUserIdxAndRemovedAtIsNull(Long userIdx);
    Long countByRemovedAtIsNull();
    Long countByIdxInAndRemovedAtIsNull(List<Long> boardIdx);
    Long countByUserAndRemovedAtIsNull(User user);

    Long countByCafe(Cafe cafe);
    Long countByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(String boardTitle, String boardContent);
}
