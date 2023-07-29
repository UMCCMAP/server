package com.umc.cmap.domain.user.login.controller;

import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.SessionUser;
import com.umc.cmap.domain.user.login.service.CustomOAuth2UserService;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService userService;


    @GetMapping("/users/login")
    public String index(HttpServletRequest request){
        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");

        if(loginUser!=null){
            Optional<User> user = userRepository.findByEmail(loginUser.getEmail());
            if(user.get().getNickname() == null){
                //새로 가입한 유저는 닉네임을 받기 위해 이동
                return "redirect:/users/nickname";
            }
        }
        return "redirect/";
    }


    @PostMapping("/users/nickname")
    public String nickname(@RequestParam("nickname") String nickname){

        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");
        userService.setNickname(loginUser.getEmail(), nickname);

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
