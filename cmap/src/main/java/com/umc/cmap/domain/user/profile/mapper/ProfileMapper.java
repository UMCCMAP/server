package com.umc.cmap.domain.user.profile.mapper;

import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.profile.dto.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    ProfileResponse toResponse(Profile profile, Long userIdx);
}
