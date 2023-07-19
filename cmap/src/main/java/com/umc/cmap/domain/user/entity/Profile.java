package com.umc.cmap.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private String cafeInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    private User user;


    @Builder
    public Profile(String user_img, String user_info, String cafe_img, String cafe_info, User user){
        this.user = user;
        this.userImg = userImg;
        this.userInfo = userInfo;
        this.cafeImg = cafeImg;
        this.cafeInfo = cafeInfo;
    }
}
