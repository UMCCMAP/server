package com.umc.cmap.domain.comment.repository;

import com.umc.cmap.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardIdx(Long boardIdx, Pageable pageable);
    Long countByUserIdx(Long userIdx);

    List<Comment> findAllByUserIdx(Long userIdx, Pageable pageable);
}
