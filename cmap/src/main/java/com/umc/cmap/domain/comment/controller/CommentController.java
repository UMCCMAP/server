package com.umc.cmap.domain.comment.controller;

import com.umc.cmap.domain.comment.dto.CommentRequest;
import com.umc.cmap.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class CommentController {
    private final CommentService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{postIdx}/comments")
    public void create(@PathVariable Long postIdx, @RequestBody @Valid final CommentRequest dto) {
        service.save(postIdx, dto);
    }
}
