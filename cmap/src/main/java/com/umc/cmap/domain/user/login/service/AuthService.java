package com.umc.cmap.domain.user.login.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.SessionUser;
import com.umc.cmap.domain.user.login.token.TokenService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class   AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TokenService tokenService;


    public User getUser(ServletRequest request) throws BaseException {
        String token = ((HttpServletRequest)request).getHeader("Authorization");
        return userRepository.findByEmail(tokenService.getUserEmail(token))
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }

    public Optional<Profile> getUserProfile(ServletRequest request) throws BaseException{
        User user = getUser(request);
        return profileRepository.findByUserIdx(user.getIdx());
    }
}
