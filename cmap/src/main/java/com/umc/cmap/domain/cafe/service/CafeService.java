package com.umc.cmap.domain.cafe.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final ThemeRepository themeRepository;

    public Cafe getCafeById(Long idx) throws BaseException {
        return cafeRepository.findById(idx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
    }

    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    public Cafe createCafe(Cafe cafe) {
        return cafeRepository.save(cafe);
    }

    public Cafe updateCafe(Long idx, Cafe cafe) throws BaseException {
        Cafe existingCafe = getCafeById(idx);

        Boolean visited = cafe.getVisited() == null ? existingCafe.getVisited() : cafe.getVisited();
        Boolean wantToVisit = cafe.getWantToVisit() == null ? existingCafe.getWantToVisit() : cafe.getWantToVisit();

        Cafe updatedCafe = Cafe.builder()
                .idx(existingCafe.getIdx())
                .name(existingCafe.getName())
                .location(existingCafe.getLocation())
                .info(existingCafe.getInfo())
                .visited(visited)
                .wantToVisit(wantToVisit)
                .build();

        return cafeRepository.save(updatedCafe);
    }

    public void deleteCafe(Long idx) throws BaseException {
        Cafe cafe = getCafeById(idx);
        cafeRepository.delete(cafe);
    }

    public List<Cafe> getVisitedCafes() throws BaseException {
        List<Cafe> visitedCafes = cafeRepository.findByVisited(true);
        if (visitedCafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.VISITED_CAFES_NOT_FOUND);
        }
        return visitedCafes;
    }

    public List<Cafe> getWantToVisitCafes() throws BaseException {
        List<Cafe> wantToVisitCafes = cafeRepository.findByWantToVisit(true);
        if (wantToVisitCafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.WANT_TO_VISIT_CAFES_NOT_FOUND);
        }
        return wantToVisitCafes;
    }

    public List<Cafe> getCafesByTheme(String themeName) throws BaseException {
        List<Cafe> cafesWithTheme = cafeRepository.findByCafeTheme_Theme_Name(themeName);

        if (cafesWithTheme.isEmpty()) {
            throw new BaseException(BaseResponseStatus.THEME_CAFES_NOT_FOUND);
        }

        return cafesWithTheme;
    }
}
