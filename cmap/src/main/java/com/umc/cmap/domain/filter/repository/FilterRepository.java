package com.umc.cmap.domain.filter.repository;

import com.umc.cmap.domain.filter.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FilterRepository extends JpaRepository<Filter, Long> {

}
