package com.umc.cmap.domain.review.controller;

import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map/place")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{cafeIndex}/reviews")
    public List<ReviewResponse> getAll(@PathVariable Long cafeIndex, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return reviewService.getAll(cafeIndex, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cafe-reviews/{reviewId}")
    public ReviewResponse getOne(@PathVariable Long reviewId) {
        return reviewService.getOne(reviewId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{cafeIndex}/review")
    public void create(@PathVariable Long cafeIndex, @RequestBody @Valid final ReviewRequest dto) {
        reviewService.save(cafeIndex, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/cafe-reviews/{reviewIndex}")
    public void update(@PathVariable Long reviewIndex, @RequestBody @Valid final ReviewRequest dto) {
        reviewService.update(reviewIndex, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cafe-reviews/{reviewIndex}")
    public void delete(@PathVariable Long reviewIndex) {
        reviewService.delete(reviewIndex);
    }
}
