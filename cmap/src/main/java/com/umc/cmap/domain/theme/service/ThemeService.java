package com.umc.cmap.domain.theme.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme createTheme(String themeName) {
        Theme theme = new Theme();
        theme.setName(themeName);

        return themeRepository.save(theme);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public Theme getThemeByName(String name) throws BaseException {
        return themeRepository.findByName(name)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_THEME_NOT_FOUND));
    }

    public Theme updateTheme(String name, String newThemeName) throws BaseException {
        Theme theme = getThemeByName(name);
        theme.setName(newThemeName);

        return themeRepository.save(theme);
    }

    public void deleteTheme(String name) throws BaseException {
        Theme theme = getThemeByName(name);
        themeRepository.delete(theme);
    }
}
