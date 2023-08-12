package com.umc.cmap.domain.user.profile.mapper;

import com.umc.cmap.domain.user.entity.Mates;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.profile.dto.MatesInfoMapping;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    ProfileResponse toResponse(Profile profile, String userNickname, Long reviewNo, Long boardNo, List<MatesInfoMapping> matesInfoList);
}
