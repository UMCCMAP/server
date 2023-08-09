package com.umc.cmap.domain.user.repository;

import com.umc.cmap.domain.user.entity.Mates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MatesRepository extends JpaRepository<Mates, Long> {
    //Long countByFromIdxAndToIdx(Long fromIdx, Long toIdx);

    @Modifying
    @Transactional
    void deleteByFromIdxAndToIdx(Long fromIdx, Long toIdx);

    List<Mates> findAllByFromIdx(Long fromIdx);
}
