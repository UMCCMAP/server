package com.umc.cmap.domain.cafe.repository;

import com.umc.cmap.domain.cafe.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long locationIdx);

}
