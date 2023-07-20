package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.dto.BoardMyPostResponse;
import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequset;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;


    /**
     * 게시판 메인 화면
     * @param pageable
     * @return
     * @throws BaseException
     */
    public Page<BoardResponse> getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), board.getCreatedAt()));
    }

    /**
     * 게시글 작성
     * @param request
     * @return
     * @throws BaseException
     */
    @Transactional
    public Long writeBoard(BoardWriteRequset request) throws BaseException {
        User user = userRepository.findById(request.getUser().getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));
        Cafe cafe = cafeRepository.findById(request.getCafe().getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_CAFE));

        Board board = Board.builder()
                .user(user)
                .cafe(cafe)
                .boardTitle(request.getBoardTitle())
                .boardContent(request.getBoardContent())
                .role(Role.ROLE_USER)
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getIdx();
    }

    /**
     * 게시글 화면
     * @param boardIdx
     * @return
     * @throws BaseException
     */
    public BoardMyPostResponse getMyPost(Long boardIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardIdx));
        return new BoardMyPostResponse(board);
    }

}
