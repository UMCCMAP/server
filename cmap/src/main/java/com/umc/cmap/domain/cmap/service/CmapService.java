package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
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
    private final CafeRepository cafeRepository;


    /**
     * 노깨 공간
     */
    /*
    public List<CmapCafeDto> getCafesByUser(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Cmap> cafes = cmapRepository.findByUser(user);

        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        return cafes.stream()
                .map(cmap -> {
                    Cafe cafe = cmap.getCafe();
                    CmapCafeDto cmapCafeDto = new CmapCafeDto(
                            cmap.getIdx(),
                            cmap.getUser().getIdx(),
                            cafe.getIdx(),
                            cmap.getType().toString(),
                            cafe.getName(),
                            cafe.getCity(),
                            cafe.getDistrict(),
                            cafe.getLocation().getLatitude(),
                            cafe.getLocation().getLongitude()
                    );
                    return cmapCafeDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Cmap createOrUpdateCmap(CmapCafeDto cmapCafeDto) throws BaseException {
        User user = userRepository.findById(cmapCafeDto.getUserId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cmapCafeDto.getCafeIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

        Type newType = Type.valueOf(cmapCafeDto.getType().toUpperCase());

        List<Cmap> existingCmaps = cmapRepository.findByUserAndCafe(user, cafe);

        for (Cmap existingCmap : existingCmaps) {
            if (existingCmap.getType() != newType) {
                existingCmap.setType(newType);
                return cmapRepository.save(existingCmap); // 이미 존재하는 Cmap의 타입 변경 후 저장하여 반환
            }
        }

        // 일치하는 Cmap이 없을 경우 새로 생성
        Cmap cmap = Cmap.builder()
                .user(user)
                .cafe(cafe)
                .type(newType)
                .build();

        return cmapRepository.save(cmap);
    }*/
    public CmapListResponse getCafesByUser(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Cmap> cafes = cmapRepository.findByUser(user);

        if (cafes.isEmpty()) {
            throw new BaseException(BaseResponseStatus.CAFE_NOT_FOUND_FOR_USER);
        }

        List<CmapCafeDto> cmapCafeDtos = new ArrayList<>();
        for (Cmap cmap : cafes) {
            Cafe cafe = cmap.getCafe();
            Location location = cafe.getLocation();

            CmapCafeDto cmapCafeDto = new CmapCafeDto(
                    cmap.getIdx(),
                    cmap.getUser().getIdx(),
                    cafe.getIdx(),
                    cmap.getType().toString(),
                    cafe.getName(),
                    cafe.getCity(),
                    cafe.getDistrict(),
                    location.getLatitude(),
                    location.getLongitude()
            );

            cmapCafeDtos.add(cmapCafeDto);
        }

        List<HashMap<Long, String>> themeList = getThemeIdxAndName();
        return new CmapListResponse(cmapCafeDtos, themeList);
    }

    @Transactional
    public CmapListResponse createOrUpdateCmap(CmapCafeDto cmapCafeDto) throws BaseException {
        User user = userRepository.findById(cmapCafeDto.getUserId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cmapCafeDto.getCafeIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

        Type newType = Type.valueOf(cmapCafeDto.getType().toUpperCase());

        List<Cmap> existingCmaps = cmapRepository.findByUserAndCafe(user, cafe);

        for (Cmap existingCmap : existingCmaps) {
            if (existingCmap.getType() != newType) {
                existingCmap.setType(newType);
                cmapRepository.save(existingCmap);
                List<CmapCafeDto> cmapCafeDtos = getCafesByUser(user.getIdx()).getCmapCafeDtos();
                return new CmapListResponse(cmapCafeDtos, getThemeIdxAndName());
            }
        }

        Cmap cmap = Cmap.builder()
                .user(user)
                .cafe(cafe)
                .type(newType)
                .build();

        cmapRepository.save(cmap);
        List<CmapCafeDto> cmapCafeDtos = getCafesByUser(user.getIdx()).getCmapCafeDtos();
        return new CmapListResponse(cmapCafeDtos, getThemeIdxAndName());
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
