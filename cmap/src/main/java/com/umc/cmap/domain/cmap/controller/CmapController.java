package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cmap.dto.CmapCafeDto;
import com.umc.cmap.domain.cmap.dto.CmapDto;
import com.umc.cmap.domain.cmap.dto.CmapListResponse;
import com.umc.cmap.domain.cmap.dto.CmapResponse;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.service.CmapService;
import com.umc.cmap.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cmap")
public class CmapController {
    private final CmapService cmapService;

    /**
     * 노깨 공간
     */

    @GetMapping("/user-default")
    public ResponseEntity<List<CmapResponse>> getCafesByUser(HttpServletRequest request) throws BaseException {
        List<CmapResponse> response = cmapService.getCafesByUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CmapResponse> createOrUpdateCmap(@RequestBody CmapDto cmapRequest, HttpServletRequest request) throws BaseException {
        CmapResponse response = cmapService.createOrUpdateCmap(cmapRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException ex) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(ex.getStatus());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/want")
    public BaseResponse<CmapListResponse> getCmapWantList(@RequestParam(required = false) List<Long> themeIdx, HttpServletRequest token) throws BaseException {
        /*
        if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapWantListWithThemeList(type, themeIdx, token));
        } else {*/
        return new BaseResponse<>(cmapService.getCmapWantList(Type.WANT, token));
        //}
    }

    @GetMapping("/went")
    public BaseResponse<CmapListResponse> getCmapWentList(@RequestParam(required = false) List<Long> themeIdx, HttpServletRequest token) throws BaseException {
        /*if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapWentListWithThemeList(themeIdx));
        } else {*/
        return new BaseResponse<>(cmapService.getCmapWentList(Type.WENT, token));
        //}
    }
}