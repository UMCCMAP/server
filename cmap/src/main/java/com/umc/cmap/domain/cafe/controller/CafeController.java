package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.request.CafeRequest;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.cafe.service.LocationService;
import com.umc.cmap.domain.filter.entity.CafeFilter;
import com.umc.cmap.domain.filter.service.CafeFilterService;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.service.ReviewService;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes")
public class CafeController {

    private final CafeService cafeService;
    private final CafeFilterService cafeFilterService;
    private final ThemeRepository themeRepository;
    private final LocationService locationService;
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<CafeResponse>> getAllCafes() {
        List<Cafe> cafes = cafeService.getAllCafes();
        List<CafeResponse> cafeResponses = cafes.stream()
                .map(cafe -> new CafeResponse(cafe, new ArrayList<>()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(cafeResponses);
    }

    @GetMapping("/{idx}")
    public ResponseEntity<CafeResponse> getCafeById(@PathVariable Long idx) throws BaseException {
        Cafe cafe = cafeService.getCafeById(idx);
        List<Review> reviews = reviewService.getCafeReviews(cafe);
        CafeResponse cafeResponse = new CafeResponse(cafe, reviews);
        return ResponseEntity.ok(cafeResponse);
    }

    @GetMapping("/name")
    public ResponseEntity<List<CafeResponse>> getCafesByName(@RequestParam(name = "name") String cafeName) throws BaseException {
        List<Cafe> cafesByName = cafeService.getCafesByName(cafeName);

        List<CafeResponse> cafeResponsesByName = new ArrayList<>();
        for (Cafe cafe : cafesByName) {
            List<Review> reviews = reviewService.getCafeReviews(cafe);
            cafeResponsesByName.add(new CafeResponse(cafe, reviews));
        }

        return ResponseEntity.ok(cafeResponsesByName);
    }

    @PostMapping
    public ResponseEntity<Cafe> createCafe(@RequestBody CafeRequest cafeRequest) throws BaseException {
        Cafe createdCafe = cafeService.createCafe(cafeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafe);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<Cafe> updateCafe(@PathVariable Long idx, @RequestBody CafeRequest updatedCafeRequest) throws BaseException {
        Cafe updatedCafe = cafeService.updateCafe(idx, updatedCafeRequest);
        return ResponseEntity.ok(updatedCafe);
    }


    @DeleteMapping("/{idx}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long idx) throws BaseException {
        cafeService.deleteCafe(idx);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException ex) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(ex.getStatus());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/filter")
    public ResponseEntity<CafeResponse> getCafesByFilter(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "district", required = false) String district,
            @RequestParam(name = "theme", required = false) List<String> themeNames) throws BaseException {

        List<CafeResponse> cafeResponses = cafeFilterService.getRandomCafeByTheme(city, district, themeNames);


        int randomIndex = new Random().nextInt(cafeResponses.size());
        CafeResponse randomCafeResponse = cafeResponses.get(randomIndex);

        return ResponseEntity.ok(randomCafeResponse);
    }

    @PostMapping("/{idx}/image")
    public ResponseEntity<String> uploadCafeImage(@PathVariable Long idx, @RequestParam("imageUrl") String imageUrl) throws BaseException {
        cafeService.uploadCafeImage(idx, imageUrl);
        return ResponseEntity.ok("성공적으로 이미지 업로드");
    }

    @GetMapping("/{idx}/image")
    public ResponseEntity<String> getCafeImage(@PathVariable Long idx) throws BaseException {
        String imageUrl = cafeService.getCafeImage(idx);
        if (imageUrl == null) {
            throw new BaseException(BaseResponseStatus.CAFE_IMAGE_NOT_FOUND);
        }
        return ResponseEntity.ok(imageUrl);
    }


    @GetMapping("/theme-all")
    public ResponseEntity<List<CafeResponse>> getCafesByTheme(
            @RequestParam(name = "themeName") String themeName) throws BaseException {
        List<CafeResponse> cafeResponses = cafeFilterService.getCafesByTheme(themeName);
        return ResponseEntity.ok(cafeResponses);
    }


}
