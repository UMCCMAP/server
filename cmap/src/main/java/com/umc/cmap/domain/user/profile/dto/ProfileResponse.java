package com.umc.cmap.domain.user.profile.dto;

import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.user.entity.Mates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String userNickname;
    private String userName;
    private String userEmail;

    private String userImg;
    private String userInfo;
    private String cafeImg;
    private String cafeTitle;
    private String cafeInfo;

    //리뷰 수, 게시글 수
    private Long reviewNo;
    private Long boardNo;

    //팔로잉 정보
    List<MatesInfoMapping> matesInfoList;
}
