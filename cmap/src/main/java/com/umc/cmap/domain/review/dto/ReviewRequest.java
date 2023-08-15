package com.umc.cmap.domain.review.dto;

import com.umc.cmap.domain.cafe.entity.Cafe;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    private Integer score;
    private String title;
    @Size(max = 20)private String keyword;
    @NotNull private String content;
    @URL private List<String> imageUrls;
    private Cafe cafe;
}
