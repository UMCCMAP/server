package com.umc.cmap.domain.user.mates.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.MatesRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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

    public void followRequest(String userNickname, HttpServletRequest request, String checkFollow) throws BaseException{
        User from = authService.getUser(request);
        User to = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if(checkFollow.equals("follow")){
            if(matesRepository.findByFromIdxAndToIdx(from.getIdx(), to.getIdx()).isEmpty()){
                follow(from, to);
            }
        }
        else if(checkFollow.equals("unfollow")){
            if(matesRepository.findByFromIdxAndToIdx(from.getIdx(), to.getIdx()).isPresent()){
                deleteByFromIdxAndToIdx(from.getIdx(), to.getIdx());
            }
        }
    }

}
