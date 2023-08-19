package com.umc.cmap.domain.cmap.mapper;

import com.umc.cmap.domain.cmap.dto.CmapStateRequest;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CmapMapper {
    Cmap toEntity(CmapStateRequest dto, User user);
}
