package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.controller.response.CafeResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
import com.umc.cmap.domain.cmap.dto.*;
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
    private final CafeService cafeService;

    /**
     * 노깨 공간
     */

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

    @GetMapping("/user-default/search") //유저 cmap검색(want)
    public ResponseEntity<List<CafeResponse>> searchCafesByNameForUserDefault(@RequestParam String cafeName, HttpServletRequest request) throws BaseException {
        List<CafeResponse> cafeResponses = cmapService.searchCafesByNameForUserDefault(cafeName, request);
        return new ResponseEntity<>(cafeResponses, HttpStatus.OK);
    }

    @GetMapping("/user-went/search")    //유저 cmap검색(went)
    public ResponseEntity<List<CafeResponse>> searchCafesByNameForUserWent(@RequestParam String cafeName, HttpServletRequest request) throws BaseException {
        List<CafeResponse> cafeResponses = cmapService.searchCafesByNameForUserWent(cafeName, request);
        return new ResponseEntity<>(cafeResponses, HttpStatus.OK);
    }

    @GetMapping("/mates-default/search")    ///메이트 cmap검색(want)
    public ResponseEntity<List<CafeResponse>> searchMatesCafeDefaultListByName(@RequestParam String mateNickname, @RequestParam String cafeName) throws BaseException {
        List<CafeResponse> cafeResponses = cmapService.searchMatesCafeDefaultListByName(mateNickname, cafeName);
        return new ResponseEntity<>(cafeResponses, HttpStatus.OK);
    }

    @GetMapping("/mates-went/search")   ///메이트 cmap검색(went)
    public ResponseEntity<List<CafeResponse>> searchMatesWentCafeListByName(@RequestParam String mateNickname, @RequestParam String cafeName) throws BaseException {
        List<CafeResponse> cafeResponses = cmapService.searchMatesWentCafeListByName(mateNickname, cafeName);
        return new ResponseEntity<>(cafeResponses, HttpStatus.OK);
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