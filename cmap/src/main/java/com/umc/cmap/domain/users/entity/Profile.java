package com.umc.cmap.domain.users.entity;

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
    private Users user;


    @Builder
    public Profile(String user_img, String user_info, String cafe_img, String cafe_info, Users user){
        this.user = user;
        this.user_img = user_img;
        this.user_info = user_info;
        this.cafe_img = cafe_img;
        this.cafe_info = cafe_info;
    }
}
