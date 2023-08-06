package com.umc.cmap.domain.user.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
}
