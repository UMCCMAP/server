package com.umc.cmap.domain.filter.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.controller.response.CafeTypeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.filter.repository.CafeFilterRepository;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.service.ReviewService;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CafeFilterService {

    private final CafeFilterRepository cafefilterRepository;
    private final CafeService cafeService;
    private final AuthService authService;
    private final ReviewService reviewService;
    private final CmapRepository cmapRepository;
    private final CafeRepository cafeRepository;


    public CafeFilterService(CafeFilterRepository cafefilterRepository, CafeService cafeService, AuthService authService, ReviewService reviewService, CmapRepository cmapRepository, CafeRepository cafeRepository) {
        this.cafefilterRepository = cafefilterRepository;
        this.cafeService=cafeService;
        this.authService=authService;
        this.reviewService = reviewService;
        this.cmapRepository = cmapRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<CafeTypeResponse> getCafesByThemes(List<String> themeNames, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        List<CafeTypeResponse> cafeTypeResponses = new ArrayList<>();

        try {
            List<Cafe> cafesWithThemes = cafeService.getCafesByThemes(themeNames);

            for (Cafe cafe : cafesWithThemes) {
                List<Review> reviews = reviewService.getCafeReviews(cafe);

                Optional<Cmap> cmapOptional = cmapRepository.findByUserAndCafe(user, cafe);
                Type userType = cmapOptional.map(Cmap::getType).orElse(null);

                List<String> cafeThemeNames = cafe.getCafeThemes().stream()
                        .map(cafeTheme -> cafeTheme.getTheme().getName())
                        .collect(Collectors.toList());

                if (cafeThemeNames.containsAll(themeNames)) {
                    cafeTypeResponses.add(new CafeTypeResponse(cafe, reviews, userType));
                }
            }

            if (cafeTypeResponses.isEmpty()) {
                throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
            }

            return cafeTypeResponses;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }
    }

    public List<CafeTypeResponse> getRandomCafeByTheme(
            String city, String district, List<String> themeNames, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);

        List<Cafe> cafes = cafeRepository.findCafesByCityAndDistrict(city, district);
        List<CafeTypeResponse> cafeTypeResponses = new ArrayList<>();

        for (Cafe cafe : cafes) {
            List<String> cafeThemeNames = cafe.getCafeThemes().stream()
                    .map(cafeTheme -> cafeTheme.getTheme().getName())
                    .collect(Collectors.toList());

            if (cafeThemeNames.containsAll(themeNames)) {
                List<Review> reviews = reviewService.getCafeReviews(cafe);

                Optional<Cmap> cmapOptional = cmapRepository.findByUserAndCafe(user, cafe);
                Type userType = cmapOptional.map(Cmap::getType).orElse(null);

                cafeTypeResponses.add(new CafeTypeResponse(cafe, reviews, userType));
            }
        }

        if (cafeTypeResponses.isEmpty()) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }

        return cafeTypeResponses;
    }



}