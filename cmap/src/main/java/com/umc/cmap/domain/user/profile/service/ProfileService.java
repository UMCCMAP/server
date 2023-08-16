package com.umc.cmap.domain.user.profile.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.review.repository.ReviewRepository;
import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.profile.dto.MatesInfoMapping;
import com.umc.cmap.domain.user.profile.dto.ProfileRequest;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import com.umc.cmap.domain.user.profile.mapper.ProfileMapper;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final MatesRepository matesRepository;
    private final ProfileMapper profileMapper;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;

    public ProfileResponse getOne(String userNickname) throws BaseException{
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserIdx(user.getIdx())
                .orElseGet(() -> profileRepository.save(Profile.builder().user(user)
                        .userImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/d3522cb0-a73e-4e24-b918-69aa14665a7f.png")
                        .cafeImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/f25830b3-5a90-4715-ade4-8651a08c4e77.png")
                        .build()));

        List<Mates> matesList = matesRepository.findAllByFromIdx(user.getIdx());
        List<MatesInfoMapping> matesInfoList = new ArrayList<>();
        if(!matesList.isEmpty()){
            for(Mates mates: matesList){
                List<MatesInfoMapping> matesInfo = profileRepository.findProfileByUserIdx(mates.getTo().getIdx());
                matesInfoList.addAll(matesInfo);
            }
        }

        Long reviewNo = reviewRepository.countByUserIdx(user.getIdx());
        Long boardNo = boardRepository.countByUserIdxAndRemovedAtIsNull(user.getIdx());

        return profileMapper.toResponse(profile, profile.getUser().getNickname(), reviewNo, boardNo, matesInfoList);
    }


    @Transactional
    public ProfileResponse update(String userNickname, ProfileRequest request) throws BaseException {
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Profile profile = profileRepository.findByUserIdx(user.getIdx())
                .orElseGet(() -> profileRepository.save(Profile.builder().user(user)
                        .userImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/d3522cb0-a73e-4e24-b918-69aa14665a7f.png")
                        .cafeImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/f25830b3-5a90-4715-ade4-8651a08c4e77.png")
                        .build()));


        profile.update(request.getUserNickname(), request.getUserImg(), request.getUserInfo(), request.getCafeImg(), request.getCafeInfo());
        profileRepository.save(profile);

        List<Mates> matesList = matesRepository.findAllByFromIdx(user.getIdx());
        List<MatesInfoMapping> matesInfoList = new ArrayList<>();
        if(!matesList.isEmpty()){
            for(Mates mates: matesList){
                List<MatesInfoMapping> matesInfo = profileRepository.findProfileByUserIdx(mates.getTo().getIdx());
                matesInfoList.addAll(matesInfo);
            }
        }

        Long reviewNo = reviewRepository.countByUserIdx(user.getIdx());
        Long boardNo = boardRepository.countByUserIdxAndRemovedAtIsNull(user.getIdx());

        return profileMapper.toResponse(profile, profile.getUser().getNickname(), reviewNo, boardNo, matesInfoList);
    }
}
