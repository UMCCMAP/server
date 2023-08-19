package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cmap.dto.CmapStateRequest;
import com.umc.cmap.domain.cmap.service.CafeStateService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CafeStateController {
    CafeStateService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/map/place/{cafeIdx}")
    public void save(@PathVariable Long cafeIdx, @RequestBody @Valid CmapStateRequest dto, ServletRequest request) throws BaseException {
        service.save(cafeIdx, dto, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/map/{nickname}/place/{cafeIdx}")
    public void update(@PathVariable String nickname, @PathVariable Long cafeIdx,
                       @RequestBody @Valid CmapStateRequest dto, ServletRequest request) throws BaseException {
        service.update(nickname, cafeIdx, dto, request);

    }
}
