package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cmap.dto.*;
import com.umc.cmap.domain.cmap.entity.Type;
import com.umc.cmap.domain.cmap.service.CmapService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cmap")
public class CmapController {
    private final CmapService cmapService;

    @GetMapping("/user-default")    //디폴트화면
    public ResponseEntity<List<CmapResponse>> getDefaultCafesByUserDefault(HttpServletRequest request) throws BaseException {
        List<CmapResponse> response = cmapService.getCafesByUserDefault(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-went")   //went화면
    public ResponseEntity<List<CmapResponse>> getDefaultCafesByUserWENT(HttpServletRequest request) throws BaseException {
        List<CmapResponse> response = cmapService.getCafesByUserWENT(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CmapResponse> createOrUpdateCmap(@RequestBody CmapDto cmapRequest, HttpServletRequest request) throws BaseException {
        CmapResponse response = cmapService.createOrUpdateCmap(cmapRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mates-all")   //모든 mate검색
    public List<String> getAllMates(HttpServletRequest request) throws BaseException {
        return cmapService.getAllMates(request);
    }

    @GetMapping("/mates-default")   //mate의 cmap default화면
    public ResponseEntity<List<CmapResponse>> getMatesCafeDefaultList(
            @RequestParam String Nickname) throws BaseException {
        List<CmapResponse> response = cmapService.getMatesDefaultCafeList(Nickname);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mates-went")   //mate의 cmap went화면
    public ResponseEntity<List<CmapResponse>> getMatesWentCafeList(
            @RequestParam String Nickname) throws BaseException {
        List<CmapResponse> response = cmapService.getMatesWentCafeList(Nickname);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-default/search")
    public ResponseEntity<List<CmapSearchResponse>> searchCafesByNameForUserDefault(@RequestParam String cafeName, HttpServletRequest request) throws BaseException {
        List<CmapSearchResponse> cmapSearchResponses = cmapService.searchCafesByNameForUserDefault(cafeName, request);
        return new ResponseEntity<>(cmapSearchResponses, HttpStatus.OK);
    }

    @GetMapping("/user-went/search")
    public ResponseEntity<List<CmapSearchResponse>> searchCafesByNameForUserWent(@RequestParam String cafeName, HttpServletRequest request) throws BaseException {
        List<CmapSearchResponse> cmapSearchResponses = cmapService.searchCafesByNameForUserWent(cafeName, request);
        return new ResponseEntity<>(cmapSearchResponses, HttpStatus.OK);
    }
    @GetMapping("/mates-default/search")
    public ResponseEntity<List<CmapSearchResponse>> searchMatesCafeDefaultListByName(@RequestParam String Nickname, @RequestParam String cafeName) throws BaseException {
        List<CmapSearchResponse> cmapSearchResponses = cmapService.searchMatesCafeDefaultListByName(Nickname, cafeName);
        return new ResponseEntity<>(cmapSearchResponses, HttpStatus.OK);
    }

    @GetMapping("/mates-went/search")
    public ResponseEntity<List<CmapSearchResponse>> searchMatesWentCafeListByName(@RequestParam String Nickname, @RequestParam String cafeName) throws BaseException {
        List<CmapSearchResponse> cmapSearchResponses = cmapService.searchMatesWentCafeListByName(Nickname, cafeName);
        return new ResponseEntity<>(cmapSearchResponses, HttpStatus.OK);
    }

    @GetMapping("/want")
    public BaseResponse<CmapListResponse> getCmapWantList(@RequestParam(required = false) List<Long> themeIdx, String nickName) throws BaseException {
        if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapListWithThemeList(Type.WANT, themeIdx, nickName));
        } else {
        return new BaseResponse<>(cmapService.getCmapList(Type.WANT, nickName));
        }
    }

    @GetMapping("/went")
    public BaseResponse<CmapListResponse> getCmapWentList(@RequestParam(required = false) List<Long> themeIdx, String nickName) throws BaseException {
        if (themeIdx != null && !themeIdx.isEmpty()) {
            return new BaseResponse<>(cmapService.getCmapListWithThemeList(Type.WENT, themeIdx, nickName));
        } else {
        return new BaseResponse<>(cmapService.getCmapList(Type.WENT, nickName));
        }
    }
}