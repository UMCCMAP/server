package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.Role;
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

    Page<Board> findByCafeInAndRemovedAtIsNull(List<Cafe> cafe, Pageable pageable);

    Page<Board> findByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(String boardTitle, String boardContent, Pageable pageable);

    Long countByUserIdxAndRemovedAtIsNull(Long userIdx);

    Long countByRemovedAtIsNull();

    Long countByIdxInAndRemovedAtIsNull(List<Long> boardIdx);

    Long countByUserAndRemovedAtIsNull(User user);

    Long countByCafeInAndRemovedAtIsNull(List<Cafe> cafe);

    Long countByCafeAndRemovedAtIsNull(Cafe cafe);

    Long countByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(String boardTitle, String boardContent);

    List<Board> findTop2ByRoleLike(Role role, Pageable pageable);
}
