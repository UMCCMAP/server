package com.umc.cmap.domain.filter.service;

import com.umc.cmap.domain.filter.dto.CafeFilterDto;
import com.umc.cmap.domain.filter.entity.CafeFilter;
import com.umc.cmap.domain.filter.repository.CafeFilterRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class CafeFilterService {

    private final CafeFilterRepository cafefilterRepository;

    public CafeFilterService(CafeFilterRepository cafefilterRepository) {
        this.cafefilterRepository = cafefilterRepository;
    }

    public CafeFilterDto getRandomCafeByTheme(String theme) {
        List<CafeFilter> cafesWithTheme = cafefilterRepository.findByFilter_Name(theme);
        if (cafesWithTheme.isEmpty()) {
            return null;
        }

        int randomIndex = new Random().nextInt(cafesWithTheme.size());
        CafeFilter randomCafeFilter = cafesWithTheme.get(randomIndex);

        return CafeFilterDto.builder()
                .idx(randomCafeFilter.getIdx())
                .name(randomCafeFilter.getCafe().getName())
                .theme(randomCafeFilter.getCafe().getCafeTheme().getTheme().getName())
                .cafeIdx(randomCafeFilter.getCafe().getIdx())
                .build();
    }

}
