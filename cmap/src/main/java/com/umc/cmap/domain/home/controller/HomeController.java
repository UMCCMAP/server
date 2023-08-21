package com.umc.cmap.domain.home.controller;

import com.umc.cmap.domain.home.dto.HomeResponse;
import com.umc.cmap.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/home")
    public List<HomeResponse> get(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return service.get(pageable);
    }
}
