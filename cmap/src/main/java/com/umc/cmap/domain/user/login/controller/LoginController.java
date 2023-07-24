package com.umc.cmap.domain.user.login.controller;

import com.umc.cmap.domain.user.login.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");

        if (loginUser != null) {
           model.addAttribute("user", loginUser);
        }
        return "/";
    }

    @GetMapping("/user")
    public String user(Model model){
        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");
        model.addAttribute("user",loginUser);
        return "/nickname";
    }
}
