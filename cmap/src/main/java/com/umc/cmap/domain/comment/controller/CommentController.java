package com.umc.cmap.domain.comment.controller;

import com.umc.cmap.domain.comment.dto.CommentRequest;
import com.umc.cmap.domain.comment.dto.CommentResponse;
import com.umc.cmap.domain.comment.dto.UpdateCommentRequest;
import com.umc.cmap.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class CommentController {
    private final CommentService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{postIdx}/comments")
    public List<CommentResponse> getAll(@PathVariable Long postIdx, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return service.getAll(postIdx, pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{postIdx}/comments")
    public void create(@PathVariable Long postIdx, @RequestBody @Valid final CommentRequest dto) {
        service.save(postIdx, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/post-comments/{commentIdx}")
    public void update(@PathVariable Long commentIdx, @RequestBody @Valid final UpdateCommentRequest dto) {
        service.update(commentIdx, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/post-comments/{commentIdx}")
    public void delete(@PathVariable Long commentIdx) {
        service.delete(commentIdx);
    }

}
