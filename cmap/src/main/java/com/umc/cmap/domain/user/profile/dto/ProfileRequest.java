package com.umc.cmap.domain.user.profile.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    private String userNickname;

    private String userImg;
    private String userInfo;
    private String cafeImg;
    private String cafeTitle;
    private String cafeInfo;
}
