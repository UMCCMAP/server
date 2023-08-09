package com.umc.cmap.domain.user.login.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.SessionUser;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.login.service.CustomOAuth2UserService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService userService;
    private final AuthService authService;
    private final ProfileRepository profileRepository;


    @GetMapping("/users/login")
    public String login(){
        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");

        if(loginUser!=null){
            Optional<User> user = userRepository.findByEmail(loginUser.getEmail());
            if(user.isPresent() && user.get().getNickname() == null){
                //새로 가입한 유저는 닉네임을 받기 위해 이동
                return "redirect:/users/nickname";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/users/nickname")
    public String showNicknamePage(){
        return "users/nickname";
    }

    @PostMapping("/users/nickname")
    public String nickname(@NotNull @RequestParam("nickname") String nickname, RedirectAttributes redirectAttributes) throws BaseException {
        if (nickname.trim().isEmpty()) {
            // 닉네임이 비어있는 경우
            redirectAttributes.addFlashAttribute("errorMessage", "닉네임을 입력해주세요.");
            return "redirect:/users/nickname";
        }
        else if(userRepository.findByNickname(nickname).isPresent()){
            //중복 처리
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 닉네임입니다.");
            return "redirect:/users/nickname";
        }


        User user = authService.getUser();
        userService.setNickname(user.getEmail(), nickname);

        //프로필 생성
        Profile profile = profileRepository.save(Profile.builder().user(user).build());

        return "redirect:/main";
    }


    @GetMapping("/")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
        }

        return "redirect:/main";
    }

}
