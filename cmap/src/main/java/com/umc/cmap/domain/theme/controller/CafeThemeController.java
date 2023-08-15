package com.umc.cmap.domain.theme.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.theme.controller.request.CafeThemeRequest;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.service.CafeThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/cafethemes")
public class CafeThemeController {
    private final CafeThemeService cafeThemeService;

    @GetMapping
    public ResponseEntity<List<CafeTheme>> getAllCafeThemes() {
        List<CafeTheme> cafeThemes = cafeThemeService.getAllCafeThemes();
        return ResponseEntity.ok(cafeThemes);
    }

    @GetMapping("/{cafeThemeId}")
    public ResponseEntity<CafeTheme> getCafeThemeById(@PathVariable Long cafeThemeId) throws BaseException {
        CafeTheme cafeTheme = cafeThemeService.getCafeThemeById(cafeThemeId);
        return ResponseEntity.ok(cafeTheme);
    }

    @PostMapping
    public ResponseEntity<List<CafeTheme>> createCafeThemes(@RequestBody List<CafeThemeRequest> requests) throws BaseException {
        List<CafeTheme> createdCafeThemes = new ArrayList<>();
        for (CafeThemeRequest request : requests) {
            String themeName = request.getThemeName();
            Long cafeIdx = request.getCafeIdx();
            CafeTheme createdCafeTheme = cafeThemeService.createCafeTheme(themeName, cafeIdx);
            createdCafeThemes.add(createdCafeTheme);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafeThemes);
    }



    @PutMapping("/{cafeThemeId}")
    public ResponseEntity<CafeTheme> updateCafeTheme(@PathVariable Long cafeThemeId, @RequestBody CafeThemeRequest cafeThemeRequest) throws BaseException {
        CafeTheme updatedCafeTheme = cafeThemeService.updateCafeTheme(cafeThemeId, cafeThemeRequest);
        return ResponseEntity.ok(updatedCafeTheme);
    }

    @DeleteMapping("/theme/{cafeThemeId}")
    public ResponseEntity<Void> deleteCafeTheme(@PathVariable Long cafeThemeId) throws BaseException {
        cafeThemeService.deleteCafeTheme(cafeThemeId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{cafeId}")
    public ResponseEntity<Void> deleteCafeThemes(@PathVariable Long cafeId) throws BaseException {
        cafeThemeService.deleteCafeThemes(cafeId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException ex) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(ex.getStatus());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
