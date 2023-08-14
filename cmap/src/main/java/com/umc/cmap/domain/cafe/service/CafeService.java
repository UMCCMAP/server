package com.umc.cmap.domain.cafe.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.request.CafeRequest;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cafe.repository.LocationRepository;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final ThemeRepository themeRepository;
    private final LocationRepository locationRepository;

    public Cafe getCafeById(Long idx) throws BaseException {
        return cafeRepository.findById(idx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
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

    public List<Cafe> getCafesByName(String cafeName) {
        return cafeRepository.findByNameContaining(cafeName);
    }


    public void uploadCafeImage(Long idx, MultipartFile imageFile) throws BaseException {
        Cafe cafe = getCafeById(idx);

        if (imageFile != null) {
            try {
                byte[] imageData = imageFile.getBytes();
                cafe.setImage(imageData);
                cafeRepository.save(cafe);
            } catch (IOException e) {
                throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_UPLOADED2);
            }
        } else {
            throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_UPLOADED);
        }
    }

    public byte[] getCafeImage(Long idx) throws BaseException {
        Cafe cafe = getCafeById(idx);
        byte[] image = cafe.getImage();
        if (image == null) {
            throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_FOUND);
        }
        return image;
    }

}
