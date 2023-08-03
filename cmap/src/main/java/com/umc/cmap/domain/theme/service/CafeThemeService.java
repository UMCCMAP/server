package com.umc.cmap.domain.theme.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.repository.CafeThemeRepository;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umc.cmap.config.BaseResponseStatus;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CafeThemeService {
    private final CafeThemeRepository cafeThemeRepository;
    private final ThemeRepository themeRepository;
    private final CafeRepository cafeRepository;

    public CafeThemeService(CafeThemeRepository cafeThemeRepository, ThemeRepository themeRepository, CafeRepository cafeRepository) {
        this.cafeThemeRepository = cafeThemeRepository;
        this.themeRepository = themeRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<CafeTheme> getAllCafeThemes() {
        return cafeThemeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CafeTheme getCafeThemeById(Long cafeThemeId) throws BaseException {

        CafeTheme cafeTheme = cafeThemeRepository.findById(cafeThemeId).orElse(null);
        if (cafeTheme == null) {
            return null;
        }
        Hibernate.initialize(cafeTheme.getTheme());
        return cafeTheme;

    }

    @Transactional
    public CafeTheme createCafeTheme(String themeName, Long cafeIdx) throws BaseException {
        Theme theme = themeRepository.findByName(themeName)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.THEME_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

        CafeTheme cafeTheme = new CafeTheme();
        cafeTheme.setTheme(theme);
        cafeTheme.setCafe(cafe);

        return cafeThemeRepository.save(cafeTheme);
    }

    @Transactional
    public CafeTheme updateCafeTheme(Long cafeThemeId, CafeTheme cafeTheme) throws BaseException {
        CafeTheme existingCafeTheme = getCafeThemeById(cafeThemeId);

        CafeTheme updatedCafeTheme = CafeTheme.builder()
                .idx(existingCafeTheme.getIdx())
                .theme(existingCafeTheme.getTheme())
                .build();

        return cafeThemeRepository.save(updatedCafeTheme);
    }

    @Transactional
    public void deleteCafeTheme(Long cafeThemeId) throws BaseException {
        CafeTheme existingCafeTheme = getCafeThemeById(cafeThemeId);
        cafeThemeRepository.delete(existingCafeTheme);
    }
}