package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cmap.dto.CmapCafeResponse;
import com.umc.cmap.domain.cmap.dto.CmapStateRequest;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.mapper.CmapMapper;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.review.service.ReviewPreviewService;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CafeStateService {
    CmapRepository cmapRepository;
    CmapMapper cmapMapper;
    CafeRepository cafeRepository;
    BoardRepository boardRepository;
    ReviewPreviewService reviewPreviewService;
    AuthService authService;

    @Transactional
    public void save(Long cafeIdx, CmapStateRequest dto, ServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Cafe cafe = getCafe(cafeIdx);
        dto.setCafe(cafe);
        if (getCmap(user, cafe) == null) {
            cmapRepository.save(cmapMapper.toEntity(dto, user));
            log.info("[cmap-save] user: {}, cafe: {}", user.getIdx(), cafe.getIdx());
        }
    }

    public CmapCafeResponse getCafeInfo(Long cafeIdx, ServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Cafe cafe = getCafe(cafeIdx);
        log.info("");
        return getCmapCafeResponse(user, cafe);
    }

    private CmapCafeResponse getCmapCafeResponse(User user, Cafe cafe) {
        return CmapCafeResponse.builder()
                .scoreAvg(reviewPreviewService.getScoreAvg(cafe.getIdx()))
                .boardCnt(boardRepository.countByCafeAndRemovedAtIsNull(cafe))
                .reviewCnt(reviewPreviewService.getReviewCntByCafe(cafe))
                .type(getCmapType(user, cafe))
                .build();
    }

    private Type getCmapType(User user, Cafe cafe) {
        Cmap cmap = getCmap(user, cafe);
        if (cmap == null) {
            return null;
        }
        return cmap.getType();
    }

    private Cafe getCafe(Long cafeIdx) {
        return cafeRepository.findById(cafeIdx).orElseThrow(EntityExistsException::new);
    }

    private Cmap getCmap(User user, Cafe cafe) {
        return cmapRepository.findByUserAndCafe(user, cafe).orElse(null);
    }


    @Transactional
    public void update(Long cafeIdx, CmapStateRequest dto, ServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Cmap cmap = cmapRepository.findByUserAndCafe(user, cafeRepository.findById(cafeIdx).orElseThrow(EntityNotFoundException::new)).orElseThrow(EntityNotFoundException::new);
        log.info("[cmap-update] cafe: {}, user: {}, type: {}", cmap.getCafe().getIdx(), cmap.getUser().getIdx(), dto.getType());

        cmap.update(dto.getType());
    }

}
