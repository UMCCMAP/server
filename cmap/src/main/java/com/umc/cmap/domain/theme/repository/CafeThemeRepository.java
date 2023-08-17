package com.umc.cmap.domain.theme.repository;

import com.umc.cmap.domain.theme.entity.CafeTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CafeThemeRepository extends JpaRepository<CafeTheme, Long> {
    @Query("SELECT cafeTheme.theme.idx FROM CafeTheme cafeTheme WHERE cafeTheme.cafe.idx = :cafeIdx")
    List<Long> findThemeIdxByCafeIdx(@Param("cafeIdx") Long cafeIdx);
}
