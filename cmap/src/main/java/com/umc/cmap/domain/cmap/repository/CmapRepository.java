package com.umc.cmap.domain.cmap.repository;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CmapRepository extends JpaRepository<Cmap, Long> {
    Optional<List<Cmap>> findByTypeAndUserIdx(Type type, Long userIdx);
    List<Cmap> findByUser(User user);
    List<Cmap> findByCafe(Cafe cafe);

}