package com.umc.cmap.domain.user.login.service;

import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.OAuthAttributes;
import com.umc.cmap.domain.user.repository.UserRepository;
import com.umc.cmap.domain.user.login.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails(). getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());

        var memberAttribute = attributes.convertToMap();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }


    public void setNickname(String email, String nickname){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setNickname(nickname);
            userRepository.save(user);
        }
    }
}