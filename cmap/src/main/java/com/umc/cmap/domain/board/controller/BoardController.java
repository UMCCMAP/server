package com.umc.cmap.domain.board.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.board.dto.*;
import com.umc.cmap.domain.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> writeBoard(@RequestBody BoardWriteRequest request, HttpServletRequest token) throws BaseException {
        return new BaseResponse<>(boardService.writeBoard(request, token));
    }

    @GetMapping("/{boardIdx}")
    public BaseResponse<BoardPostViewResponse> getMyPost(@PathVariable Long boardIdx, HttpServletRequest token) throws BaseException {
        return new BaseResponse<>(boardService.getPostView(boardIdx, token));
    }

    @DeleteMapping("/{boardIdx}")
    public BaseResponse<String> deletePost(@PathVariable Long boardIdx, HttpServletRequest token) throws BaseException {
        return new BaseResponse<>(boardService.deletePost(boardIdx, token));
    }

    @PatchMapping("/{boardIdx}")
    public BaseResponse<String> modifyPost(@PathVariable Long boardIdx, @RequestBody BoardModifyRequest request,
                                           HttpServletRequest token) throws BaseException {
        return new BaseResponse<>(boardService.modifyPost(boardIdx, request, token));
    }

    @PostMapping("/{boardIdx}/like")
    public BaseResponse<String> likePost(@PathVariable Long boardIdx, @RequestParam boolean type,
                                         HttpServletRequest token) throws BaseException {
        if (!type) {
            return new BaseResponse<>(boardService.likePostCancel(boardIdx, token));
        } else {
            return new BaseResponse<>(boardService.likePost(boardIdx, token));
        }
    }

    @GetMapping("/search")
    public BaseResponse<BoardListResponse> getBoardBySearch(@PageableDefault(size = 5, sort = "idx", direction = DESC) Pageable pageable,
                                                            @RequestParam String searchType,
                                                            String keyword) throws BaseException {
        if ("title-content".equalsIgnoreCase(searchType)) {
            return new BaseResponse<>(boardService.getBoardBySearchOfTitle(pageable, keyword));
        } else if ("writer".equalsIgnoreCase(searchType)) {
            return new BaseResponse<>(boardService.getBoardByWriter(pageable, keyword));
        } else if ("cafe".equalsIgnoreCase(searchType)) {
            return new BaseResponse<>(boardService.getBoardByCafe(pageable, keyword));
        } else {
            throw new IllegalArgumentException("Invalid searchType: " + searchType);
        }
    }

    @GetMapping("/my-posts")
    public BaseResponse<Page<BoardResponse>> getMyBoardList(@PageableDefault(size = 5, sort = "idx", direction = DESC) Pageable pageable,
                                                            HttpServletRequest token) throws BaseException {
        return new BaseResponse<>(boardService.getMyBoardList(pageable,token));
    }

}
