package com.umc.cmap.domain.user.mates.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MatesService {
    private final MatesRepository matesRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public void follow(User from, User to) throws BaseException {

        Mates mates = new Mates(from, to);
        matesRepository.save(mates);
    }

    public void deleteByFromIdxAndToIdx(Long fromIdx, Long toIdx){
        matesRepository.deleteByFromIdxAndToIdx(fromIdx, toIdx);
    }

    /*public boolean find(Long fromIdx, Long toIdx){
        if(matesRepository.countByFromIdxAndToIdx(fromIdx, toIdx)==0)
            return false;
        return true;
    }*/ //로그인한 유저와 프로필 유저가 동일한 사람인지 체크하는 메소드 -> 다른 사람이면 팔로잉 버튼 활성화
}
