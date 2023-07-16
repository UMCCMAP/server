package com.umc.cmap.domain.board.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.board.dto.BoardResponseDto;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시판 main page 불러오기
     * @param pageable
     * @return
     * @throws BaseException
     */
    @GetMapping
    public BaseResponse<Page<BoardResponseDto>> getBoard(@PageableDefault(size = 4, sort = "idx", direction = DESC) Pageable pageable) throws BaseException {
        return new BaseResponse<>(boardService.getBoardList(pageable));
    }
}
