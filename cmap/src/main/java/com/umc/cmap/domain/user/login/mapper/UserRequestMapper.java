package com.umc.cmap.domain.user.login.mapper;

import com.umc.cmap.domain.user.login.dto.UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    public UserRequest toRequest(OAuth2User oAuth2User){
        var attributes = oAuth2User.getAttributes();
        return UserRequest.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .build();
    }
}
