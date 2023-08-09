package com.umc.cmap.domain.review.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriterResponse {
    private Long userIdx;
    private String userNickname;
    private String userImg;
}
