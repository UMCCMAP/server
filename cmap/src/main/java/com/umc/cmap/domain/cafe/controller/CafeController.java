package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.filter.service.CafeFilterService;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

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
    public ResponseEntity<Cafe> getCafeByTheme(@RequestParam("theme") String themeName) throws BaseException {
        List<Cafe> cafesWithTheme = cafeService.getCafesByTheme(themeName);

        int randomIndex = new Random().nextInt(cafesWithTheme.size());
        Cafe randomCafe = cafesWithTheme.get(randomIndex);

        return ResponseEntity.ok(randomCafe);
    }



}