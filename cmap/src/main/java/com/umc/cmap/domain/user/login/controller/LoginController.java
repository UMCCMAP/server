package com.umc.cmap.domain.user.login.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.login.service.CustomOAuth2UserService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService userService;
    private final AuthService authService;
    private final ProfileRepository profileRepository;


    @GetMapping("/users/login")
    public String login(HttpServletRequest request, Authentication authentication) throws BaseException{
        User user = authService.getUser(request);

        if(user!=null && user.getNickname() == null){
            //새로 가입한 유저는 닉네임을 받기 위해 이동
            return "redirect:/users/nickname";
        }

        return "redirect:/";
    }

    @GetMapping("/users/nickname")
    public String showNicknamePage(){
        return "users/nickname";
    }

    @PostMapping("/users/nickname")
    public String nickname(@NotNull @RequestBody Map<String, String> nicknameMap, HttpServletRequest request, RedirectAttributes redirectAttributes) throws BaseException {
        if (nicknameMap.get("nickname").trim().isEmpty()) {
            // 닉네임이 비어있는 경우
            redirectAttributes.addFlashAttribute("errorMessage", "닉네임을 입력해주세요.");
            return "redirect:/users/nickname";
        }
        else if(userRepository.findByNickname(nicknameMap.get("nickname")).isPresent()){
            if(userRepository.findByNickname(nicknameMap.get("nickname")).get().getNickname().toLowerCase().equals(nicknameMap.get("nickname").toLowerCase())){
                //중복 처리
                redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 닉네임입니다.");
                return "redirect:/users/nickname";
            }
        }


        User user = authService.getUser(request);
        userService.setNickname(user.getEmail(), nicknameMap.get("nickname"));

        //프로필 생성
        Profile profile = profileRepository.save(Profile.builder().user(user).build());

        return "redirect:/main";
    }
}
