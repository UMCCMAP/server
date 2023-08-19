package com.umc.cmap.domain.filter.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.filter.dto.CafeFilterDto;
import com.umc.cmap.domain.filter.entity.CafeFilter;
import com.umc.cmap.domain.filter.repository.CafeFilterRepository;
import com.umc.cmap.domain.review.entity.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CafeFilterService {

    private final CafeFilterRepository cafefilterRepository;
    private final CafeService cafeService;


    public CafeFilterService(CafeFilterRepository cafefilterRepository,CafeService cafeService) {
        this.cafefilterRepository = cafefilterRepository;
        this.cafeService=cafeService;
    }

    public List<CafeResponse> getCafesByTheme(String theme) throws BaseException {
        List<CafeResponse> cafeResponses = new ArrayList<>();

        try {
            List<Cafe> cafesWithTheme = cafeService.getCafesByThemes(Collections.singletonList(theme));

            for (Cafe cafe : cafesWithTheme) {
                cafeResponses.add(new CafeResponse(cafe, new ArrayList<>()));
            }

            return cafeResponses;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }
    }

    public List<CafeResponse> getRandomCafeByTheme(String city, String district, List<String> themeNames) throws BaseException {
        List<CafeResponse> cafeResponses = new ArrayList<>();

        try {
            if ((city != null && district != null) && (themeNames != null && themeNames.size() >= 1)) {
                List<Cafe> cafes = cafeService.getCafesByCityAndDistrictAndThemes(city, district, themeNames);

                for (Cafe cafe : cafes) {
                    List<String> cafeThemeNames = cafe.getCafeThemes().stream()
                            .map(cafeTheme -> cafeTheme.getTheme().getName())
                            .collect(Collectors.toList());

                    if (cafeThemeNames.containsAll(themeNames)) {
                        cafeResponses.add(new CafeResponse(cafe, new ArrayList<>()));
                    }
                }
            } else if ((city != null && district != null) && (themeNames == null || themeNames.isEmpty())) {
                List<Cafe> cafes = cafeService.getCafesByCityAndDistrict(city, district);
                cafeResponses = cafes.stream()
                        .map(cafe -> new CafeResponse(cafe, new ArrayList<>()))
                        .collect(Collectors.toList());
            } else if (themeNames != null && themeNames.size() >= 1 && (city == null || district == null)) {
                List<Cafe> cafesWithThemes = cafeService.getCafesByThemes(themeNames);

                for (Cafe cafe : cafesWithThemes) {
                    List<String> cafeThemeNames = cafe.getCafeThemes().stream()
                            .map(cafeTheme -> cafeTheme.getTheme().getName())
                            .collect(Collectors.toList());

                    if (cafeThemeNames.containsAll(themeNames)) {
                        cafeResponses.add(new CafeResponse(cafe, new ArrayList<>()));
                    }
                }
            }

            if (cafeResponses.isEmpty()) {
                throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
            }

            return cafeResponses;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }
    }

}
