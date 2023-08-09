package com.umc.cmap.domain.user.mates.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.mates.service.MatesService;
import com.umc.cmap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatesController {
    private final MatesService matesService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/users/profile/{userNickname}/follow")
    public String follow(@PathVariable String userNickname) throws BaseException{
        User from = authService.getUser();
        User to = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        matesService.follow(from, to);

        return "redirect:/users/profile/{userNickname}";
    }

    @PostMapping("/users/profile/{userNickname}/unfollow")
    public String unfollow(@PathVariable String userNickname) throws BaseException {
        Long fromIdx = authService.getUser().getIdx();
        Long toIdx = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND))
                .getIdx();

        matesService.deleteByFromIdxAndToIdx(fromIdx, toIdx);
        return "redirect:/users/profile/{userNickname}";
    }
}
