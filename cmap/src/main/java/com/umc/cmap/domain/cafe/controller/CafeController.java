package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.filter.service.CafeFilterService;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping
    public List<Cafe> getAllCafes() {
        return cafeService.getAllCafes();
    }

    @GetMapping("/{idx}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable Long idx) throws BaseException {
        Cafe cafe = cafeService.getCafeById(idx);
        return ResponseEntity.ok(cafe);
    }

    @GetMapping("/name")
    public ResponseEntity<List<CafeResponse>> getCafesByName(@RequestParam(name = "name") String cafeName) throws BaseException {
        List<Cafe> cafesByName = cafeService.getCafesByName(cafeName);
        List<CafeResponse> cafeResponsesByName = cafesByName.stream()
                .map(CafeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cafeResponsesByName);
    }



    @PostMapping
    public ResponseEntity<Cafe> createCafe(@RequestBody Cafe cafe) {
        Cafe createdCafe = cafeService.createCafe(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafe);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<Cafe> updateCafe(@PathVariable Long idx, @RequestBody Cafe cafe) throws BaseException {
        Cafe updatedCafe = cafeService.updateCafe(idx, cafe);
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

    @GetMapping("/visited")
    public List<Cafe> getVisitedCafes() throws BaseException {
        return cafeService.getVisitedCafes();
    }

    @GetMapping("/wantToVisit")
    public List<Cafe> getWantToVisitCafes() throws BaseException {
        return cafeService.getWantToVisitCafes();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CafeResponse>> getCafesByFilter(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "district", required = false) String district,
            @RequestParam(name = "theme", required = false) List<String> themeNames) throws BaseException {

        List<CafeResponse> cafeResponses = new ArrayList<>();

        if ((city != null && district != null) && (themeNames == null || themeNames.isEmpty())) {
            List<Cafe> cafes = cafeService.getCafesByCityAndDistrict(city, district);
            cafeResponses = cafes.stream()
                    .map(CafeResponse::new)
                    .collect(Collectors.toList());
        } else if (themeNames != null && !themeNames.isEmpty() && (city == null || district == null)) {
            List<Cafe> cafesWithThemes = cafeService.getCafesByThemes(themeNames);

            for (Cafe cafe : cafesWithThemes) {
                List<String> cafeThemeNames = cafe.getCafeThemes().stream()
                        .map(cafeTheme -> cafeTheme.getTheme().getName()) // Get the theme name from CafeTheme
                        .collect(Collectors.toList());

                if (cafeThemeNames.containsAll(themeNames)) {
                    cafeResponses.add(new CafeResponse(cafe));
                }
            }
        }

        if (cafeResponses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int randomIndex = new Random().nextInt(cafeResponses.size());
        CafeResponse randomCafeResponse = cafeResponses.get(randomIndex);

        return ResponseEntity.ok(Collections.singletonList(randomCafeResponse));
    }


}