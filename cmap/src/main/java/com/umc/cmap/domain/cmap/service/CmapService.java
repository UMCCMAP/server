package com.umc.cmap.domain.cmap.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cmap.repository.CmapRepository;
import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.profile.dto.MatesInfoMapping;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class CmapService {
    private final CmapRepository cmapRepository;
    private final MatesRepository matesRepository;
    private final UserRepository userRepository;

    public List<String> getMates(String userNickname) throws BaseException{
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Mates> matesList = matesRepository.findAllByFromIdx(user.getIdx());
        List<String> matesNicknameList = new ArrayList<>();
        for(Mates mates: matesList){
            String matesInfo = userRepository.findByIdx(mates.getTo().getIdx()).get().getNickname();
            matesNicknameList.add(matesInfo);
        }
        return matesNicknameList;
    }
}
