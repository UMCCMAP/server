package com.umc.cmap.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_idx")
    private Long idx;

    private String userImg;

    private String userInfo;

    private String cafeImg;

    private String cafeTitle;

    private String cafeInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_idx")
    private User user;


    @Builder
    public Profile(String userImg, String userInfo, String cafeImg, String cafeTitle, String cafeInfo, User user){
        this.user = user;
        this.userImg = userImg;
        this.userInfo = userInfo;
        this.cafeImg = cafeImg;
        this.cafeTitle=cafeTitle;
        this.cafeInfo = cafeInfo;
    }


    public void update(String userNickname, String userImg, String userInfo, String cafeImg, String cafeTitle, String cafeInfo){
        this.user.setNickname(userNickname);
        this.userImg = userImg;
        this.userInfo = userInfo;
        this.cafeImg = cafeImg;
        this.cafeTitle=cafeTitle;
        this.cafeInfo = cafeInfo;
    }
}
