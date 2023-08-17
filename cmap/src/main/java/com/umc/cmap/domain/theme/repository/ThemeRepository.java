package com.umc.cmap.domain.theme.repository;

import com.umc.cmap.domain.theme.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByName(String name);
    boolean existsByName(String name);
}