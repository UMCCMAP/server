package com.umc.cmap.domain.review.mapper;

import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.review.entity.ReviewImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewImageMapper {

    ReviewImage toEntity(String imageUrl, Review review);
}
