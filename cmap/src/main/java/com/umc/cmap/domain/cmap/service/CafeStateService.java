package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cmap.dto.CmapStateRequest;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.mapper.CmapMapper;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
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

import static com.umc.cmap.config.BaseResponseStatus.DONT_HAVE_ACCESS;

@Slf4j
@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CafeStateService {
    CmapRepository cmapRepository;
    CmapMapper cmapMapper;
    CafeRepository cafeRepository;
    AuthService authService;

    @Transactional
    public void save(Long cafeIdx, CmapStateRequest dto, ServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Cafe cafe = cafeRepository.findById(cafeIdx).orElseThrow(EntityExistsException::new);
        dto.setCafe(cafe);
        if (getCmap(user, cafe) == null) {
            cmapRepository.save(cmapMapper.toEntity(dto, user));
        }
    }

    private Cmap getCmap(User user, Cafe cafe) {
        return cmapRepository.findByUserAndCafe(user, cafe).orElse(null);
    }

    @Transactional
    public void update(String nickname, Long cafeIdx, CmapStateRequest dto, ServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        if (!isEqualUser(nickname, user)) {
            throw new BaseException(DONT_HAVE_ACCESS);
        }

        Cmap cmap = cmapRepository.findByUserAndCafe(user, cafeRepository.findById(cafeIdx).orElseThrow(EntityNotFoundException::new)).orElseThrow(EntityNotFoundException::new);
        log.info("[cmap-update] cafe [" + cmap.getCafe().getIdx() + "] user [" + cmap.getUser().getIdx() + "] type [" + dto.getType() + "]");
        cmap.update(dto.getType());
    }

    private boolean isEqualUser(String nickname, User param) {
        return param.getNickname().equals(nickname);
    }
}
