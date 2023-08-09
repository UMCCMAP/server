package com.umc.cmap.domain.filter.repository;

import com.umc.cmap.domain.filter.entity.CafeFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeFilterRepository extends JpaRepository<CafeFilter, Long> {
    List<CafeFilter> findByFilter_Name(String filterName);
}


