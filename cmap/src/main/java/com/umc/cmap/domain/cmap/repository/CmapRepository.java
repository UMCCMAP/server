package com.umc.cmap.domain.cmap.repository;

import com.umc.cmap.domain.cmap.entity.Cmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmapRepository extends JpaRepository<Cmap, Long> {
}
