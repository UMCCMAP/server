package com.umc.cmap.domain.user.profile.dto;

public interface MatesInfoMapping {
    String getUserImg();
    User getUser();

    interface User{
        String getNickname();
    }
}
