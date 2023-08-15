package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cmap.dto.CmapListResponse;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class CmapService {
    private final CmapRepository cmapRepository;
    private final ThemeRepository themeRepository;

    /**
     * 노깨 공간
     */

    /**
     * 젼 공간
     */

    /**
     * 션 공간
     */

    /**
     * 데옹 공간
     */
    public BaseResponse<CmapListResponse> getCmapWantList(List<Long> themeIdx) throws BaseException {
        String t = "t";

        List<HashMap<Long,String>> themeList = themeRepository.findIdxAndName();
        return new BaseResponse<>(CmapListResponse);
    }

    public BaseResponse<CmapListResponse> getCmapWentList(List<Long> themeIdx) throws BaseException {
        String t = "t";

        List<HashMap<Long,String>> themeList = themeRepository.findIdxAndName();
        return new BaseResponse<>(CmapListResponse);
    }
}
