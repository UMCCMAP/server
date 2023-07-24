//Entity를 바로 Session에 넣을 때 발생하는 직렬화 오류를 피하기 위한 Class

package com.umc.cmap.domain.user.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(String name, String email){
        this.name=name;
        this.email=email;
    }
}
