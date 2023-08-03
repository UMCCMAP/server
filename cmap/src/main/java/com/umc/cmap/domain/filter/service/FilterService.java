package com.umc.cmap.domain.filter.service;

import com.umc.cmap.domain.filter.entity.Filter;
import com.umc.cmap.domain.filter.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterService {

    private final FilterRepository filterRepository;

    public Filter saveFilter(Filter filter) {
        return filterRepository.save(filter);
    }

    public List<Filter> getAllFilters() {
        return filterRepository.findAll();
    }


}
