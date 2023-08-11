package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
