package com.umc.cmap.domain.cafe.repository;

import com.umc.cmap.domain.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    List<Cafe> findByVisited(boolean visited);
    List<Cafe> findByWantToVisit(boolean wantToVisit);

    List<Cafe> findByCafeTheme_Theme_Name(String themeName);
}

