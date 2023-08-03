package com.umc.cmap.domain.board.repository;

import com.umc.cmap.domain.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT idx, tagName FROM Tag")
    List<Map<Long, String>> findAllTags();

    List<Tag> findAllByTagIdxIn(List<Long> tagIdx);
}
