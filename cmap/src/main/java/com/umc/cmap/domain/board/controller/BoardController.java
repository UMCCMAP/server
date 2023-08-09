package com.umc.cmap.domain.board.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.board.dto.*;
import com.umc.cmap.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
    public BaseResponse<BoardListResponse> getBoard(@PageableDefault(size = 4, sort = "idx", direction = DESC) Pageable pageable,
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
    public BaseResponse<BoardMyPostResponse> getMyPost(@PathVariable Long boardIdx) throws BaseException {
        return new BaseResponse<>(boardService.getMyPost(boardIdx));
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
    public BaseResponse<String> likePost(@PathVariable Long boardIdx, @RequestBody LikeBoardRequest request) throws BaseException {
        if (!request.isType()) {
            return new BaseResponse<>(boardService.likePostCancel(boardIdx, request.getUserIdx()));
        } else {
            return new BaseResponse<>(boardService.likePost(boardIdx, request.getUserIdx()));
        }
    }

}
