package com.umc.cmap.domain.profile.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.profile.dto.ProfileRequest;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import com.umc.cmap.domain.user.profile.service.ProfileService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private ProfileService service;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void update_profile() throws BaseException{

        Long userIdx = 2L;
        String userImg = "https://user-image/2";
        String userInfo = "안녕하세요.";
        String cafeImg = "https://cafe-image/1";
        String cafeInfo = "짱짱";

        ProfileRequest request = ProfileRequest.builder()
                .userImg(userImg)
                .userInfo(userInfo)
                .cafeImg(cafeImg)
                .cafeInfo(cafeInfo)
                .build();

        service.update(userIdx, request);
        System.out.println("request: "+request.getCafeInfo());

        Profile profile = profileRepository.findByUserIdx(userIdx).get();
        assertThat(profile.getCafeInfo()).isEqualTo(request.getCafeInfo());
        System.out.println("profile: "+profile.getCafeInfo());
    }

    @Test
    void getOne_test(){
        Long userIdx = 2L;
        ProfileResponse profileResponse = service.getOne(userIdx);
        System.out.println(profileResponse.getUserImg());
        assertThat(profileResponse.getUserImg()).isEqualTo("https://user-image/2");
    }
}
