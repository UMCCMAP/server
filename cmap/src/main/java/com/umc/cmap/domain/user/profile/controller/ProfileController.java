package com.umc.cmap.domain.user.profile.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.profile.dto.ProfileRequest;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import com.umc.cmap.domain.user.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final AuthService authService;
    private final ReviewRepository reviewService;

    @GetMapping("/users/profile/{userNickname}")
    public ProfileResponse profile(@PathVariable String userNickname) throws BaseException{
        return profileService.getOne(userNickname);
    }

    @PatchMapping("/users/profile/{userNickname}")
    public String editProfile(@PathVariable String userNickname, @RequestBody ProfileRequest profileRequest) throws BaseException {
        User user = authService.getUser();
        if(user.getNickname().equals(userNickname)){
            profileService.update(profileRequest);
        }

        return "redirect:/users/profile/{userNickname}";
    }

    @GetMapping("/users/profile/{userNickname}/reviews")
    public void userReview() throws BaseException{
        User user = authService.getUser();
        //need reviewService
    }
}
