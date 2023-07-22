package com.umc.cmap.domain.review.dto;

import com.umc.cmap.domain.cafe.entity.Cafe;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    private Double score;
    @NotNull private String content;
    private List<String> imageUrls;
    private Cafe cafe;
}
