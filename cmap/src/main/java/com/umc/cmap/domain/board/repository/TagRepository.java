package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.dto.TagDto;
import com.umc.cmap.domain.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT new com.umc.cmap.domain.board.dto.TagDto(idx, tagName) FROM Tag")
    List<TagDto> findAllTags();
}
