package com.umc.cmap.domain.user.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequest {
    private String email;
    private String name;

    @Builder
    public UserRequest(String email, String name){
        this.email=email;
        this.name=name;
    }
}
