package com.umc.cmap.domain.theme.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.theme.controller.request.CafeThemeRequest;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<List<Theme>> createThemes(@RequestBody List<CafeThemeRequest> requests) throws BaseException {
        List<Theme> createdThemes = new ArrayList<>();
        for (CafeThemeRequest request : requests) {
            String themeName = request.getThemeName();
            Theme createdTheme = themeService.createTheme(themeName);
            createdThemes.add(createdTheme);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdThemes);
    }


    @GetMapping //테마이름전체찾기
    public ResponseEntity<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Theme> getThemeByName(@PathVariable String name) throws BaseException {
        Theme theme = themeService.getThemeByName(name);
        return ResponseEntity.ok(theme);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Theme> updateTheme(@PathVariable String name, @RequestBody CafeThemeRequest request) throws BaseException {
        String newThemeName = request.getThemeName();
        Theme updatedTheme = themeService.updateTheme(name, newThemeName);
        return ResponseEntity.ok(updatedTheme);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTheme(@PathVariable String name) throws BaseException {
        themeService.deleteTheme(name);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException ex) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(ex.getStatus());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
