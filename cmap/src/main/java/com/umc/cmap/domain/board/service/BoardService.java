package com.umc.cmap.domain.board.service;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    // 임시로 생성
    public List<Board> loadBoard() throws ResponseException {
        return boardRepository.findAll();
    }

}
