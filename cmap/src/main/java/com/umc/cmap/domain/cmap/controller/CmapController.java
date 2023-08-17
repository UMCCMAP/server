package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cmap.dto.CmapListResponse;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.service.CmapService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cmap")
public class CmapController {
    private final CmapService cmapService;

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
    @GetMapping("/want")
    public BaseResponse<CmapListResponse> getCmapWantList(@RequestParam(required = false) List<Long> themeIdx, HttpServletRequest token) throws BaseException {
        if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapListWithThemeList(Type.WANT, themeIdx, token));
        } else {
        return new BaseResponse<>(cmapService.getCmapList(Type.WANT, token));
        }
    }

    @GetMapping("/went")
    public BaseResponse<CmapListResponse> getCmapWentList(@RequestParam(required = false) List<Long> themeIdx, HttpServletRequest token) throws BaseException {
        if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapListWithThemeList(Type.WENT, themeIdx, token));
        } else {
        return new BaseResponse<>(cmapService.getCmapList(Type.WENT, token));
        }
    }
}