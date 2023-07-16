package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> getBoardList(Pageable pageable) throws BaseException {
        return boardRepository.findAll(pageable);
    }

}
