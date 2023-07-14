package com.umc.cmap.domain.board.controller;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
    private final BoardService boardService;

    // 임시로 생성 -> template 부재
    @GetMapping("/loadAll")
    public ResponseTemplate<List<Board>> loadAll() throws ResponseException {
        return new ResponseTemplate<>(boardService.loadBoard());
    }
}
