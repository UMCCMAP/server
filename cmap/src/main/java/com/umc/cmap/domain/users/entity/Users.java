package com.umc.cmap.domain.users.entity;


import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "Users")
public class Users extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx")
    private Long idx;

    private String name;

    private String email;

    private String password;

    @Length(min=2, max=10)
    private String nickname;


    @Builder
    public Users(Long id, String name, String email, String password, String nickname){
        this.idx=idx;
        this.name=name;
        this.email=email;
        this.password=password;
        this.nickname=nickname;
    }
}
