package com.umc.cmap.domain.cafe.repository;

import com.umc.cmap.domain.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    // List<Cafe> findByVisited(boolean visited);
    //  List<Cafe> findByWantToVisit(boolean wantToVisit);

    List<Cafe> findByCafeThemes_Theme_Name(String themeName);
    @Query("SELECT DISTINCT c FROM Cafe c JOIN FETCH c.cafeThemes ct JOIN ct.theme t WHERE t.name = :themeName")
    List<Cafe> findByCafeThemes_Theme_NameJoinFetch(@Param("themeName") String themeName);

    List<Cafe> findByCafeThemes_Theme_NameIn(List<String> themeNames);

    List<Cafe> findByCityAndDistrict(String city, String district);

    List<Cafe> findByNameContaining(String cafeName);
    Cafe findByName(String cafeName);

}

