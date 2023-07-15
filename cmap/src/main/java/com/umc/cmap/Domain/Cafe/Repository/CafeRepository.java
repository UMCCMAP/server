package com.umc.cmap.Domain.Cafe.Repository;

import com.umc.cmap.Domain.Cafe.Entity.CafeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends JpaRepository<CafeEntity, Long> {

}

