package com.umc.cmap.domain.board.service;

import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequset;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<BoardResponse> getBoardList(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), board.getCreatedAt()));
    }

    public Long writeBoard(BoardWriteRequset request) {
        Board board = Board.builder()
                .boardTitle(request.getBoardTitle())
                .boardContent(request.getBoardContent())
                // Cafe 정보 등록 로직은 추가 구현 필요
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getIdx();
    }

}
