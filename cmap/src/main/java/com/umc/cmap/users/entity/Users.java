package com.umc.cmap.users.entity;

//import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx")
    private Long id;

    private String user_name;
    private String email;
    private String password;
    private String nickname;
    private String login_method;

    @Builder
    public Users(Long id, String email, String password, String nickname, String login_method){
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.login_method = login_method;
    }
}
