package com.umc.cmap.domain.user.profile.dto;

import com.umc.cmap.domain.user.entity.Mates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String userNickname;

    private String userImg;
    private String userInfo;
    private String cafeImg;
    private String cafeInfo;

    //팔로잉 정보
    List<MatesInfoMapping> matesInfoList;
}
