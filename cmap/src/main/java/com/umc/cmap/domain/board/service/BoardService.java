package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.dto.BoardModifyRequest;
import com.umc.cmap.domain.board.dto.BoardMyPostResponse;
import com.umc.cmap.domain.board.dto.BoardResponse;
import com.umc.cmap.domain.board.dto.BoardWriteRequest;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.BoardTag;
import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.board.entity.Tag;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.board.repository.BoardTagRepository;
import com.umc.cmap.domain.board.repository.TagRepository;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardTagRepository boardTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;


    /**
     * 게시판 메인 화면 (특정 태그 선택 X)
     * @param pageable
     * @return
     * @throws BaseException
     */
    public Page<BoardResponse> getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAllByRemovedAtIsNull(pageable);
        List<Map<Long,String>> tagNames = tagRepository.findAllTags();
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagNames, board.getCreatedAt()));
    }

    /**
     * 게시판 메인 화면 (특정 태그 선택 O)
     * @param pageable
     * @param tagIdx
     * @return
     * @throws BaseException
     */
    public Page<BoardResponse> getBoardListWithTags(Pageable pageable, List<Long> tagIdx) throws BaseException {
        List<Tag> tags = tagRepository.findAllByTagIdxIn(tagIdx);
        List<Long> boardIdx = boardTagRepository.findBoardIdxByTagIn(tags);
        Page<Board> boardPage = boardRepository.findByIdxInAndRemovedAtIsNull(boardIdx, pageable);
        List<Map<Long,String>> tagNames = tagRepository.findAllTags();
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagNames, board.getCreatedAt()));
    }

    /**
     * 태그들 가져오는 코드 - 해당 boardIdx에 연관된 tagIdx를 받아오고, 이를 이용해서 tagName까지 리턴
     * 게시글 불러올 때 사용할 예정
     */
    public Map<Long, List<Map<Long, String>>> getTagsForBoardList(List<Long> boardIdxList) throws BaseException {
        return boardIdxList.stream().collect(Collectors.toMap(
                boardIdx -> boardIdx,
                boardIdx -> tagRepository.findAllByBoardIdx(boardIdx).stream()
                        .map(tag -> Map.of(tag.getIdx(), tag.getTagName()))
                        .collect(Collectors.toList())
        ));
    }
    /*
    public Map<Long, String> getTags(Long boardIdx) throws BaseException {
        Map<Long, String> tagIdxName = new HashMap<>();
        List<Long> tagIdxList = boardTagRepository.findTagIdxListByBoardIdx(boardIdx);
        tagIdxList.forEach(tagIdx -> {
            Optional<Tag> tagOptional = tagRepository.findById(tagIdx);
            tagOptional.ifPresent(tag -> {
                tagIdxName.put(tag.getIdx(), tag.getTagName());
            });
        });
        return tagIdxName;
    }*/

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
                .role(Role.USER)
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
