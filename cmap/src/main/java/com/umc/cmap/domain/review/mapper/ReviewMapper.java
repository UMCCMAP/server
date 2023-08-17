package com.umc.cmap.domain.review.mapper;

import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.dto.ReviewWriterResponse;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    ReviewResponse toResponse(Review review, List<String> imageUrls, ReviewWriterResponse userInfo);

    Review toEntity(ReviewRequest dto, User user);
}
