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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MatesController {
    private final MatesService matesService;
    private final MatesRepository matesRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PutMapping("/users/profile/{userNickname}")
    public void follow(@PathVariable String userNickname, HttpServletRequest request, @RequestBody Map<String, String> checkFollow) throws BaseException{
        User from = authService.getUser(request);
        User to = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if(checkFollow.get("checkFollow").trim().equals("follow")){
            if(matesRepository.findByFromIdxAndToIdx(from.getIdx(), to.getIdx()).isEmpty()){
                matesService.follow(from, to);
            }
        }
        else if(checkFollow.get("checkFollow").trim().equals("unfollow")){
            if(matesRepository.findByFromIdxAndToIdx(from.getIdx(), to.getIdx()).isPresent()){
                matesService.deleteByFromIdxAndToIdx(from.getIdx(), to.getIdx());
            }
        }
    }
}
