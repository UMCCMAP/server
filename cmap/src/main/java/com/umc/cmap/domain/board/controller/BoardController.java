package com.umc.cmap.domain.board.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.board.dto.BoardMyPostResponse;
import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequset;
import com.umc.cmap.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시판 메인
     * @param pageable
     * @return
     * @throws BaseException
     */
    @GetMapping
    public BaseResponse<Page<BoardResponse>> getBoard(@PageableDefault(size = 4, sort = "idx", direction = DESC) Pageable pageable) throws BaseException {
        return new BaseResponse<>(boardService.getBoardList(pageable));
    }

    /**
     * 게시글 작성
     * @param request
     * @return
     * @throws BaseException
     */
    @PostMapping("/posting")
    public BaseResponse<Long> writeBoard(@RequestBody BoardWriteRequset request) throws BaseException {
        return new BaseResponse<>(boardService.writeBoard(request));
    }

    /**
     * 게시글 화면
     * @param boardIdx
     * @return
     * @throws BaseException
     */
    @GetMapping("/mypost")
    public BaseResponse<BoardMyPostResponse> getMyPost(@PathVariable Long boardIdx) throws BaseException {
        return new BaseResponse<>(boardService.getMyPost(boardIdx));
    }

    /**
     * 게시글 공감
     */

    /**
     * 게시글 삭제
     */

    /**
     * 게시글 수정
     */

}
