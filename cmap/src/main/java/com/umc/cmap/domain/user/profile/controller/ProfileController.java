package com.umc.cmap.domain.user.profile.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.review.service.ReviewService;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.profile.dto.ProfileRequest;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import com.umc.cmap.domain.user.profile.service.ProfileService;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final AuthService authService;
    private final ReviewService reviewService;

    @GetMapping("/users/profile/{userNickname}")
    public ProfileResponse profile(@PathVariable String userNickname) throws BaseException{
        return profileService.getOne(userNickname);
    }

    @PatchMapping("/users/profile/{userNickname}")
    public String editProfile(@PathVariable String userNickname, HttpServletRequest request, @RequestBody ProfileRequest profileRequest) throws BaseException {
        User user = authService.getUser(request);
        if(user.getNickname().equals(userNickname)){
            if (profileRequest.getUserNickname().trim().isEmpty()) {
                // 닉네임이 비어있는 경우
                return "닉네임을 입력해주세요.";
            }
            else if(userRepository.findByNickname(profileRequest.getUserNickname()).isPresent()){
                if(userRepository.findByNickname(profileRequest.getUserNickname()).get().getNickname().toLowerCase().equals(profileRequest.getUserNickname().toLowerCase())){
                    //중복 처리
                    return "이미 사용 중인 닉네임입니다.";
                }
            }

            ProfileResponse profileResponse = profileService.update(userNickname, profileRequest);
            return "redirect:/users/profile/" + profileResponse.getUserNickname();
        }

        return "redirect:/users/profile/{userNickname}";
    }

    /*@GetMapping("/users/profile/{userNickname}/reviews")
    public List<ReviewResponse> userReview() throws BaseException{
        User user = authService.getUser();
        return reviewService.getAllUserReviews(user.getIdx(), @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable);
    }*/
}