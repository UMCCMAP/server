package com.umc.cmap.domain.user.login.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.dto.OAuthAttributes;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails(). getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        userRepository.findByIdx(user.getIdx())
                .orElse(userRepository.save(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        return userRepository.findByEmail(attributes.getEmail())
                .map(existingUser -> existingUser.update(existingUser.getName()))
                .orElse(attributes.toEntity());
    }

    public String checkUser(HttpServletRequest request) throws BaseException{
        User user = authService.getUser(request);

        if(user!=null && user.getNickname() == null){
            return "redirect:/users/nickname";
        }
        else if(user!=null && user.getNickname() != null){
            return user.getNickname();
        }

        return "redirect:/";
    }

    public String setNickname(HttpServletRequest request, String nickname) throws BaseException{
        if(nickname.isEmpty()){
            return "닉네임을 입력해주세요.";
        }
        else if(userRepository.findByNickname(nickname).isPresent()){
            if(userRepository.findByNickname(nickname).get().getNickname().toLowerCase().equals(nickname))
                return "이미 사용 중인 닉네임입니다.";
        }

        User user = authService.getUser(request);
        user.setNickname(nickname);
        userRepository.save(user);

        Profile profile = profileRepository.save(Profile.builder().user(user)
                .userImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/d3522cb0-a73e-4e24-b918-69aa14665a7f.png")
                .cafeImg("https://cmap-bucket.s3.ap-northeast-2.amazonaws.com/f25830b3-5a90-4715-ade4-8651a08c4e77.png")
                .build());

        return "redirect:/main";
    }
}