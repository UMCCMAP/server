package com.umc.cmap.domain.cafe.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.request.CafeRequest;
import com.umc.cmap.domain.cafe.controller.response.CafeTypeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cafe.repository.LocationRepository;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.service.ReviewService;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final ThemeRepository themeRepository;
    private final LocationRepository locationRepository;
    private final AuthService authService;
    private final CmapRepository cmapRepository;
    private final ReviewService reviewService;

    public Cafe getCafeById(Long idx) throws BaseException {
        return cafeRepository.findById(idx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
    }

    public List<Cafe> getCafesByName(String cafeName) {
        return cafeRepository.findByNameContaining(cafeName);
    }
    public CafeTypeResponse getCafeWithUserType(Long cafeIdx, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);

        Cafe cafe = getCafeById(cafeIdx);
        if (cafe == null) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND);
        }

        List<Review> reviews = reviewService.getCafeReviews(cafe);

        Optional<Cmap> cmapOptional = cmapRepository.findByUserAndCafe(user, cafe);
        if (cmapOptional.isPresent()) {
            Type userType = cmapOptional.get().getType();
            return new CafeTypeResponse(cafe, reviews, userType);
        } else {
            return new CafeTypeResponse(cafe, reviews, null);
        }
    }

    public CafeTypeResponse getCafeWithUserTypeByName(String name, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);

        List<Cafe> cafes = getCafesByName(name);
        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND);
        }

        Cafe cafe = cafes.get(0);
        List<Review> reviews = reviewService.getCafeReviews(cafe);

        Optional<Cmap> cmapOptional = cmapRepository.findByUserAndCafe(user, cafe);
        if (cmapOptional.isPresent()) {
            Type userType = cmapOptional.get().getType();
            return new CafeTypeResponse(cafe, reviews, userType);
        } else {
            return new CafeTypeResponse(cafe, reviews, null);
        }
    }


    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    @Transactional
    public Cafe createCafe(CafeRequest cafeRequest) throws BaseException {
        if (cafeRequest.getLocationIdx() == null) {
            throw new BaseException(BaseResponseStatus.LOCATION_NOT_INPUT);
        }

        Location location = locationRepository.findById(cafeRequest.getLocationIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LOCATION_NOT_FOUND));

        Cafe cafe = Cafe.builder()
                .name(cafeRequest.getName())
                .city(cafeRequest.getCity())
                .district(cafeRequest.getDistrict())
                .info(cafeRequest.getInfo())
                .location(location)
                .build();

        return cafeRepository.save(cafe);
    }

    @Transactional
    public Cafe updateCafe(Long idx, CafeRequest updatedCafeRequest) throws BaseException {
        Cafe existingCafe = getCafeById(idx);
        LocalDateTime createdAt = existingCafe.getCreatedAt();

        existingCafe = Cafe.builder()
                .idx(existingCafe.getIdx())
                .name(updatedCafeRequest.getName())
                .city(updatedCafeRequest.getCity())
                .district(updatedCafeRequest.getDistrict())
                .info(updatedCafeRequest.getInfo())
                .location(existingCafe.getLocation())
                .build();

        return cafeRepository.save(existingCafe);
    }


    public void deleteCafe(Long idx) throws BaseException {
        Cafe cafe = getCafeById(idx);
        cafeRepository.delete(cafe);
    }

    public List<Cafe> getCafesByTheme(String themeName) throws BaseException {
        List<Cafe> cafesWithTheme = cafeRepository.findByCafeThemes_Theme_Name(themeName);

        if (cafesWithTheme.isEmpty()) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }

        return cafesWithTheme;
    }

    public List<Cafe> getCafesByThemes(List<String> themeNames) {
        return cafeRepository.findByCafeThemes_Theme_NameIn(themeNames);
    }

    public List<Cafe> getCafesByCityAndDistrict(String city, String district) {
        return cafeRepository.findByCityAndDistrict(city, district);
    }

    public String getCafeImage(Long idx) throws BaseException {
        Cafe cafe = getCafeById(idx);
        String imageUrl = cafe.getImage();
        if (imageUrl == null) {
            throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_FOUND);
        }
        return imageUrl;
    }

    public void uploadCafeImage(Long idx, String imageUrl) throws BaseException {
        Cafe cafe = getCafeById(idx);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            cafe.setImage(imageUrl);
            cafeRepository.save(cafe);
        } else {
            throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_UPLOADED);
        }
    }

    public List<CafeTypeResponse> getCafesByCityAndDistrictAndThemes(
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

        return cafeTypeResponses;
    }

}
