package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.dto.TagDto;
import com.umc.cmap.domain.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT new com.umc.cmap.domain.board.dto.TagDto(idx, tagName) FROM Tag")
    List<TagDto> findAllTags();

    List<Tag> findAllByIdxIn(List<Long> idx);
}
