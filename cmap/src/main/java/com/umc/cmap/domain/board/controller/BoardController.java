package com.umc.cmap.domain.board.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.board.dto.*;
import com.umc.cmap.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;


    @GetMapping
    public BaseResponse<BoardListResponse> getBoard(@PageableDefault(size = 5, sort = "idx", direction = DESC) Pageable pageable,
                                                    @RequestParam(required = false) List<Long> tagIdx) throws BaseException {
        if (tagIdx != null && !tagIdx.isEmpty()) {
            return new BaseResponse<>(boardService.getBoardListWithTags(pageable, tagIdx));
        } else {
            return new BaseResponse<>(boardService.getBoardList(pageable));
        }
    }

    @PostMapping
    public BaseResponse<Long> writeBoard(@RequestBody BoardWriteRequest request) throws BaseException {
        return new BaseResponse<>(boardService.writeBoard(request));
    }

    @GetMapping("/{boardIdx}")
    public BaseResponse<BoardPostViewResponse> getMyPost(@PathVariable Long boardIdx) throws BaseException {
        return new BaseResponse<>(boardService.getPostView(boardIdx));
    }

    @DeleteMapping("/{boardIdx}")
    public BaseResponse<String> deletePost(@PathVariable Long boardIdx) throws BaseException {
        return new BaseResponse<>(boardService.deletePost(boardIdx));
    }

    @PatchMapping("/{boardIdx}")
    public BaseResponse<String> modifyPost(@PathVariable Long boardIdx, @RequestBody BoardModifyRequest request) throws BaseException {
        return new BaseResponse<>(boardService.modifyPost(boardIdx, request));
    }

    @PostMapping("/{boardIdx}/like")
    public BaseResponse<String> likePost(@PathVariable Long boardIdx, @RequestParam boolean type) throws BaseException {
        if (!type) {
            return new BaseResponse<>(boardService.likePostCancel(boardIdx));
        } else {
            return new BaseResponse<>(boardService.likePost(boardIdx));
        }
    }

    @GetMapping("/search")
    public BaseResponse<BoardListResponse> getBoardBySearch(@PageableDefault(size = 5, sort = "idx", direction = DESC) Pageable pageable,
                                                            @RequestParam String keyword) throws BaseException {
        return new BaseResponse<>(boardService.getBoardBySearch(pageable, keyword));
    }

    @GetMapping("/my-posts")
    public BaseResponse<Page<BoardResponse>> getMyBoardList(@PageableDefault(size = 5, sort = "idx", direction = DESC) Pageable pageable) throws BaseException {
        return new BaseResponse<>(boardService.getMyBoardList(pageable));
    }

}
