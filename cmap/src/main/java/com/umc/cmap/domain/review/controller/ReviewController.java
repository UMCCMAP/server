package com.umc.cmap.domain.review.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.review.dto.ReviewPreviewResponse;
import com.umc.cmap.domain.review.dto.ReviewRequest;
import com.umc.cmap.domain.review.dto.ReviewResponse;
import com.umc.cmap.domain.review.service.ReviewPreviewService;
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
    ReviewPreviewService reviewPreviewService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{cafeIdx}/reviews")
    public List<ReviewResponse> getAll(@PathVariable Long cafeIdx, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return reviewService.getAll(cafeIdx, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cafe-reviews/{reviewIdx}")
    public ReviewResponse getOne(@PathVariable Long reviewIdx) {
        return reviewService.getOne(reviewIdx);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{cafeIdx}/preview")
    public List<ReviewPreviewResponse> getPreview(@PathVariable Long cafeIdx, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return reviewPreviewService.get(cafeIdx, pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{cafeIdx}/review")
    public void create(@PathVariable Long cafeIdx, @RequestBody @Valid final ReviewRequest dto) throws BaseException {
        reviewService.save(cafeIdx, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/cafe-reviews/{reviewIdx}")
    public void update(@PathVariable Long reviewIdx, @RequestBody @Valid final ReviewRequest dto) {
        reviewService.update(reviewIdx, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cafe-reviews/{reviewIdx}")
    public void delete(@PathVariable Long reviewIdx) {
        reviewService.delete(reviewIdx);
    }
}
