package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.board.dto.BoardMyPostResponse;
import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequset;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.generic.IINC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    /**
     * 게시판 메인
     * @param pageable
     * @return
     */
    public Page<BoardResponse> getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), board.getCreatedAt()));
    }

    /**
     * 게시글 작성
     * @param request
     * @return
     */
    public Long writeBoard(BoardWriteRequset request) throws BaseException {
        Board board = Board.builder()
                .boardTitle(request.getBoardTitle())
                .boardContent(request.getBoardContent())
                // Cafe 정보 등록 로직은 추가 구현 필요
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getIdx();
    }

    /**
     * 게시글 화면
     */
    public BoardMyPostResponse getMyPost(Long id) throws BaseException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + id));
        String userNickname = board.getUser().getNickname();
        return new BoardMyPostResponse(board);
    }

}
