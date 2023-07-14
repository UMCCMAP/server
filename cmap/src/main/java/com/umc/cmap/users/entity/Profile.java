package com.umc.cmap.users.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "my_profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_profile_idx")
    private Long id;

    private String user_img;
    private String user_info;
    private String cafe_img;
    private String cafe_info;

    @OneToOne
    @JoinColumn(name = "user_idx")
    private Users users;

    @Builder
    public Profile(String user_img, String user_info, String cafe_img, String cafe_info, Users users){
        this.user_img = user_img;
        this.user_info = user_info;
        this.cafe_img = cafe_img;
        this.cafe_info = cafe_info;
        this.users = users;
    }
}
