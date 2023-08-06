package com.umc.cmap.domain.user.profile.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.profile.dto.ProfileRequest;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import com.umc.cmap.domain.user.profile.mapper.ProfileMapper;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileResponse getOne(String userNickname) throws BaseException{
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserIdx(user.getIdx())
                .orElseGet(() -> profileRepository.save(Profile.builder().user(user).build()));
        return profileMapper.toResponse(profile, profile.getUser().getNickname());
    }


    @Transactional
    public ProfileResponse update(String userNickname, ProfileRequest request) throws BaseException {
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserIdx(user.getIdx())
                .orElseGet(() -> profileRepository.save(Profile.builder().user(user).build()));


        profile.update(request.getUserNickname(), request.getUserImg(), request.getUserInfo(), request.getCafeImg(), request.getCafeInfo());
        profileRepository.save(profile);


        return profileMapper.toResponse(profile, profile.getUser().getNickname());
}
