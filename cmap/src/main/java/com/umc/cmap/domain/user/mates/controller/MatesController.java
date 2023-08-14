package com.umc.cmap.domain.user.mates.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.mates.service.MatesService;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatesController {
    private final MatesService matesService;
    private final MatesRepository matesRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @RequestMapping("/users/profile/{userNickname}/follow")
    public String follow(@PathVariable String userNickname, HttpServletRequest request) throws BaseException{
        User from = authService.getUser(request);
        User to = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        if(matesRepository.findByFromIdxAndToIdx(from.getIdx(), to.getIdx()).isEmpty()){
            matesService.follow(from, to);
        }

        return "redirect:/users/profile/"+userNickname;
    }

    @RequestMapping("/users/profile/{userNickname}/unfollow")
    public String unfollow(@PathVariable String userNickname, HttpServletRequest request) throws BaseException {
        Long fromIdx = authService.getUser(request).getIdx();
        Long toIdx = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND))
                .getIdx();

        if(matesRepository.findByFromIdxAndToIdx(fromIdx, toIdx).isPresent()){
            matesService.deleteByFromIdxAndToIdx(fromIdx, toIdx);
        }
        return "redirect:/users/profile/"+userNickname;
    }
}
