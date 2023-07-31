package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.dto.BoardModifyRequest;
import com.umc.cmap.domain.board.dto.BoardMyPostResponse;
import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequest;
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
        Page<Board> boardPage = boardRepository.findAllByRemovedAtIsNull(pageable);
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), board.getCreatedAt()));
    }

    /**
     * 게시글 작성
     * @param request
     * @return
     * @throws BaseException
     */
    @Transactional
    public Long writeBoard(BoardWriteRequest request) throws BaseException {
        User user = userRepository.findById(request.getUserIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Cafe cafe = cafeRepository.findById(request.getCafeIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

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
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        if(board.isDeleted()) { throw new BaseException(BaseResponseStatus.POST_DELETED); }
        return new BoardMyPostResponse(board);
    }

    /**
     * 게시글 삭제
     * @param boardIdx
     * @return
     * @throws BaseException
     */
    @Transactional
    public String deletePost(Long boardIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        board.removeBoard();
        return "게시글 삭제에 성공했습니다.";
    }

    /**
     * 게시글 수정
     * @param boardIdx
     * @return
     * @throws BaseException
     */
    @Transactional
    public String modifyPost(Long boardIdx, BoardModifyRequest request) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        Cafe cafe = cafeRepository.findById(request.getCafeIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
        board.modifyPost(cafe, request.getBoardTitle(), request.getBoardContent());

        return "게시글 수정에 성공했습니다.";
    }

}
