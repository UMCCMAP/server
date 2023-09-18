package com.umc.cmap.domain.user.login.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.login.service.CustomOAuth2UserService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final CustomOAuth2UserService userService;
    private final AuthService authService;
    private final ProfileRepository profileRepository;


    @GetMapping("/users/login")
    public String login(HttpServletRequest request) throws BaseException{
        return userService.checkUser(request);
    }

    @GetMapping("/users/nickname")
    public String showNicknamePage(){
        return "users/nickname";
    }

    @PostMapping("/users/nickname")
    public String nickname(@NotNull @RequestBody Map<String, String> nicknameMap, HttpServletRequest request) throws BaseException {
        return userService.setNickname(request, nicknameMap.get("nickname").trim());
    }
}
