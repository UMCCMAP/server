package com.umc.cmap.domain.theme.service;

import com.umc.cmap.config.BaseException;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
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

    @Transactional(readOnly = false)
    public CafeTheme getCafeThemeById(Long cafeThemeId) throws BaseException {

        CafeTheme cafeTheme = cafeThemeRepository.findById(cafeThemeId).orElse(null);
        if (cafeTheme == null) {
            throw new BaseException(BaseResponseStatus.CAFE_THEME_NOT_FOUND);
        }
        Hibernate.initialize(cafeTheme.getTheme());
        return cafeTheme;

    }

    public CafeTheme createCafeTheme(Long themeIdx, Long cafeIdx) throws BaseException {
        Theme theme = themeRepository.findById(themeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.THEME_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

        if (cafe.getCafeThemes().stream().anyMatch(ct -> ct.getTheme().equals(theme))) {
            throw new BaseException(BaseResponseStatus.CAFE_THEME_ALREADY_EXISTS);
        }

        CafeTheme cafeTheme = new CafeTheme();
        cafeTheme.setTheme(theme);
        cafeTheme.setCafe(cafe);

        cafe.getCafeThemes().add(cafeTheme);

        return cafeThemeRepository.save(cafeTheme);
    }

    @Transactional
    public CafeTheme updateCafeTheme(Long cafeThemeId, Long themeIdx) throws BaseException {
        CafeTheme existingCafeTheme = cafeThemeRepository.findById(cafeThemeId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_THEME_NOT_FOUND));

        Theme newTheme = themeRepository.findById(themeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.THEME_NOT_FOUND));

        Cafe cafe = existingCafeTheme.getCafe();

        CafeTheme cafeThemeToReplace = cafe.getCafeThemes().stream()
                .filter(ct -> ct.getIdx().equals(cafeThemeId))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_THEME_NOT_FOUND));

        cafeThemeToReplace.setTheme(newTheme);

        return cafeThemeRepository.save(cafeThemeToReplace);
    }

    @Transactional
    public void deleteCafeTheme(Long cafeThemeId) throws BaseException {
        CafeTheme existingCafeTheme = getCafeThemeById(cafeThemeId);
        cafeThemeRepository.delete(existingCafeTheme);
    }

    @Transactional
    public void deleteCafeThemes(Long cafeId) throws BaseException {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

        List<CafeTheme> cafeThemesToDelete = new ArrayList<>(cafe.getCafeThemes());

        cafe.getCafeThemes().clear();
        cafeRepository.save(cafe);

        cafeThemeRepository.deleteAll(cafeThemesToDelete);
    }

}