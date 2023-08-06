package com.umc.cmap.domain.user.login.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.SessionUser;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final HttpSession httpSession;
    private final UserRepository userRepository;

    public User getUser() throws BaseException{
        SessionUser loginUser = (SessionUser) httpSession.getAttribute("loginUser");
        User user = userRepository.findByEmail(loginUser.getEmail())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return user;
    }
}
