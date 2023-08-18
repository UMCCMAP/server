package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cmap.dto.CmapCafeDto;
import com.umc.cmap.domain.cmap.dto.CmapListResponse;
import com.umc.cmap.config.BaseResponseStatus;
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

    /**
     * 노깨 공간
     */

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
