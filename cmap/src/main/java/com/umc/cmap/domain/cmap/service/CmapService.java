package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.cmap.dto.*;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.theme.entity.Theme;
import com.umc.cmap.domain.theme.repository.CafeThemeRepository;
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
import java.util.HashSet;
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
    private final CafeThemeRepository cafeThemeRepository;
    private final CafeRepository cafeRepository;


    @Transactional
    public List<CmapResponse> getCafesByUserDefault(HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Type cmapType = Type.WANT;

        List<Cmap> cafes = cmapRepository.findByUserAndType(user, cmapType);

        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return cafes.stream()
                .map(CmapResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CmapResponse> getCafesByUserWENT(HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Type cmapType = Type.WENT;

        List<Cmap> cafes = cmapRepository.findByUserAndType(user, cmapType);

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

        List<Cmap> existingCmaps = cmapRepository.findByCafe(cafe);

        for (Cmap existingCmap : existingCmaps) {
            if (existingCmap.getType() != newType) {
                existingCmap.setType(newType);
                cmapRepository.save(existingCmap);
                return new CmapResponse(existingCmap);
            }
            else
                throw new BaseException(BaseResponseStatus.DUPLICATE_CMAP_TYPE);
        }

        Cmap cmap = Cmap.builder()
                .user(user)
                .cafe(cafe)
                .type(newType)
                .build();

        cmap = cmapRepository.save(cmap);
        return new CmapResponse(cmap);
    }

    @Transactional
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

    @Transactional
    public List<String> getAllMates(HttpServletRequest request) throws BaseException {
        User loggedInUser = authService.getUser(request);

        List<Mates> matesList = matesRepository.findAllByFromIdx(loggedInUser.getIdx());
        List<String> matesNicknames = new ArrayList<>();

        for (Mates mates : matesList) {
            String matesNickname = mates.getTo().getNickname();
            matesNicknames.add(matesNickname);
        }

        return matesNicknames;
    }

    @Transactional
    public List<CmapResponse> getMatesDefaultCafeList(String Nickname) throws BaseException {
        User mateUser = userRepository.findByNickname(Nickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Type cmapType = Type.WANT;

        List<Cmap> cafes = cmapRepository.findByUserAndType(mateUser, cmapType);

        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return cafes.stream()
                .map(CmapResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CmapResponse> getMatesWentCafeList(String Nickname) throws BaseException {
        User mateUser = userRepository.findByNickname(Nickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Type cmapType = Type.WENT;

        List<Cmap> cafes = cmapRepository.findByUserAndType(mateUser, cmapType);

        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return cafes.stream()
                .map(CmapResponse::new)
                .collect(Collectors.toList());
    }

    public List<CmapSearchResponse> searchCafesByNameForUserDefault(String cafeName, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Type cmapType = Type.WANT;

        List<Cmap> cafes = cmapRepository.findByUserAndTypeAndCafeNameContainingIgnoreCase(user, cmapType, cafeName);

        List<CmapSearchResponse> resultMap = new ArrayList<>();

        for (Cmap cmap : cafes) {
            if (cmap.getCafe().getName().equalsIgnoreCase(cafeName)) {
                resultMap.add(new CmapSearchResponse(new CafeResponse(cmap.getCafe(), cmap.getCafe().getReviews()), cmap.getType()));
            }
        }

        if (resultMap.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return resultMap;
    }

    public List<CmapSearchResponse> searchCafesByNameForUserWent(String cafeName, HttpServletRequest request) throws BaseException {
        User user = authService.getUser(request);
        Type cmapType = Type.WENT;

        List<Cmap> cafes = cmapRepository.findByUserAndTypeAndCafeNameContainingIgnoreCase(user, cmapType, cafeName);

        List<CmapSearchResponse> resultMap = new ArrayList<>();

        for (Cmap cmap : cafes) {
            if (cmap.getCafe().getName().equalsIgnoreCase(cafeName)) {
                resultMap.add(new CmapSearchResponse(new CafeResponse(cmap.getCafe(), cmap.getCafe().getReviews()), cmap.getType()));
            }
        }

        if (resultMap.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return resultMap;
    }


    public List<CmapSearchResponse> searchMatesCafeDefaultListByName(String mateNickname, String cafeName) throws BaseException {
        User mateUser = userRepository.findByNickname(mateNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Cmap> cafes = cmapRepository.findByUserAndTypeAndCafeNameContainingIgnoreCase(mateUser, Type.WANT, cafeName);

        List<CmapSearchResponse> resultMap = new ArrayList<>();

        for (Cmap cmap : cafes) {
            if (cmap.getCafe().getName().equalsIgnoreCase(cafeName)) {
                resultMap.add(new CmapSearchResponse(new CafeResponse(cmap.getCafe(), cmap.getCafe().getReviews()), cmap.getType()));
            }
        }

        if (resultMap.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return resultMap;
    }

    public List<CmapSearchResponse> searchMatesWentCafeListByName(String mateNickname, String cafeName) throws BaseException {
        User mateUser = userRepository.findByNickname(mateNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Cmap> cafes = cmapRepository.findByUserAndTypeAndCafeNameContainingIgnoreCase(mateUser, Type.WENT, cafeName);

        List<CmapSearchResponse> resultMap = new ArrayList<>();

        for (Cmap cmap : cafes) {
            if (cmap.getCafe().getName().equalsIgnoreCase(cafeName)) {
                resultMap.add(new CmapSearchResponse(new CafeResponse(cmap.getCafe(), cmap.getCafe().getReviews()), cmap.getType()));
            }
        }

        if (resultMap.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return resultMap;
    }

    public CmapListResponse getCmapList(Type type, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        List<Cmap> cmaps = cmapRepository.findByTypeAndUserIdx(type, user.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CMAP_WANT_NOT_FOUND));
        List<CmapCafeDto> CmapCafeDtos = new ArrayList<>();
        for (Cmap cmap : cmaps) {
            CmapCafeDto cmapCafeDto =  new CmapCafeDto(cmap.getCafe().getIdx(), cmap.getCafe().getImage());
            CmapCafeDtos.add(cmapCafeDto);
        }
        List<HashMap<Long, String>> themeList = getThemeIdxAndName();
        return new CmapListResponse(CmapCafeDtos, themeList);
    }

    public CmapListResponse getCmapListWithThemeList(Type type, List<Long> themes, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        List<Cmap> cmaps = cmapRepository.findByTypeAndUserIdx(type, user.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CMAP_WANT_NOT_FOUND));
        List<Cafe> cafes = getCafeContainsThemeList(cmaps, themes);
        List<CmapCafeDto> CmapCafeDtos = new ArrayList<>();
        for (Cafe cafe : cafes) {
            CmapCafeDto cmapCafeDto =  new CmapCafeDto(cafe.getIdx(), cafe.getImage());
            CmapCafeDtos.add(cmapCafeDto);
        }
        List<HashMap<Long, String>> themeList = getThemeIdxAndName();
        return new CmapListResponse(CmapCafeDtos, themeList);
    }

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

    private List<Cafe> getCafeContainsThemeList(List<Cmap> cmaps, List<Long> themes) {
        List<Cafe> cafeList = cmaps.stream().map(Cmap::getCafe).toList();
        List<Cafe> result = new ArrayList<>();
        for (Cafe cafe : cafeList){
            List<Long> themeIdxList = cafeThemeRepository.findThemeIdxByCafeIdx(cafe.getIdx());
            if (new HashSet<>(themeIdxList).containsAll(themes)) { result.add(cafe); }
        }
        return result;
    }

}
