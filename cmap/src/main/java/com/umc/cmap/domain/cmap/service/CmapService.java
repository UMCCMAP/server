package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cmap.dto.CmapCafeDto;
import com.umc.cmap.domain.cmap.dto.CmapDto;
import com.umc.cmap.domain.cmap.dto.CmapListResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cmap.dto.CmapResponse;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class CmapService {
    private final CmapRepository cmapRepository;
    private final MatesRepository matesRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final AuthService authService;
    private final CafeRepository cafeRepository;


    /**
     * 노깨 공간
     */

    @Transactional
    public List<CmapResponse> getCafesByUser(HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request); // 토큰으로부터 사용자 정보 추출
        List<Cmap> cafes = cmapRepository.findByUser(user);
        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }
        return cafes.stream()
                .map(CmapResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CmapResponse createOrUpdateCmap(CmapDto cmapRequest, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Cafe cafe = cafeRepository.findById(cmapRequest.getCafeIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
        Type newType = Type.valueOf(cmapRequest.getType().toUpperCase());

        List<Cmap> existingCmaps = cmapRepository.findByUserAndCafe(user, cafe);

        for (Cmap existingCmap : existingCmaps) {
            if (existingCmap.getType() == newType) {
                throw new BaseException(BaseResponseStatus.DUPLICATE_CMAP_TYPE);
            }
        }

        Cmap cmap = Cmap.builder()
                .user(user)
                .cafe(cafe)
                .type(newType)
                .build();

        cmap = cmapRepository.save(cmap);
        return new CmapResponse(cmap);
    }


    public List<String> getMates(String userNickname) throws BaseException{
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Mates> matesList = matesRepository.findAllByFromIdx(user.getIdx());
        List<String> matesNicknameList = new ArrayList<>();
        for(Mates mates: matesList){
            String matesInfo = userRepository.findByIdx(mates.getTo().getIdx()).get().getNickname();
            matesNicknameList.add(matesInfo);
        }
        return matesNicknameList;
    }

    /**
     * 젼 공간
     */

    /**
     * 션 공간
     */

    /**
     * 데옹 공간
     */
    public CmapListResponse getCmapWantList(Type type, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        List<Cmap> cmaps = cmapRepository.findByTypeAndUserIdx(type, user.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CMAP_WANT_NOT_FOUND));
        List<CmapCafeDto> CmapCafeDtos = new ArrayList<>();
        for (Cmap cmap : cmaps) {
            CmapCafeDto cmapCafeDto =  new CmapCafeDto(cmap.getCafe().getIdx(), cmap.getCafe().getName());
            CmapCafeDtos.add(cmapCafeDto);
        }
        List<HashMap<Long, String>> themeList = getThemeIdxAndName();
        return new CmapListResponse(CmapCafeDtos, themeList);
    }
/*
    public CmapListResponse getCmapWantListWithThemeList(String type, List<Long> themeIdx, HttpServletRequest token) throws BaseException {
        String cafeTitle = "t";
        Long cafeIdx = 1L;

        List<HashMap<Long,String>> themeList = themeRepository.findIdxAndName();
        return new CmapListResponse(cafeIdx,cafeTitle,themeList);
    }*/

    public CmapListResponse getCmapWentList(Type type, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        List<Cmap> cmaps = cmapRepository.findByTypeAndUserIdx(type, user.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CMAP_WANT_NOT_FOUND));
        List<CmapCafeDto> CmapCafeDtos = new ArrayList<>();
        for (Cmap cmap : cmaps) {
            CmapCafeDto cmapCafeDto =  new CmapCafeDto(cmap.getCafe().getIdx(), cmap.getCafe().getName());
            CmapCafeDtos.add(cmapCafeDto);
        }
        List<HashMap<Long, String>> themeList = getThemeIdxAndName();
        return new CmapListResponse(CmapCafeDtos, themeList);
    }
/*
    public CmapListResponse getCmapWentListWithThemeList(List<Long> themeIdx) throws BaseException {
        String cafeTitle = "t";
        Long cafeIdx = 1L;

        List<HashMap<Long,String>> themeList = themeRepository.findIdxAndName();
        return new CmapListResponse(cafeIdx,cafeTitle,themeList);
    }*/

    private List<HashMap<Long, String>> getThemeIdxAndName() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> {
                    HashMap<Long, String> themeMap = new HashMap<>();
                    themeMap.put(theme.getIdx(), theme.getName());
                    return themeMap;
                })
                .collect(Collectors.toList());
    }
}
