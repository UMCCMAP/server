package com.umc.cmap.domain.review.controller;

import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("cafes/{id}/reviews")
    public List<ReviewResponse> getAll(@PathVariable Long id, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return reviewService.getAll(id, pageable);
    }
}
