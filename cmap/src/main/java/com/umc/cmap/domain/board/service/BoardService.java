package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.dto.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardTagRepository boardTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;


    public Page<BoardResponse> getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAllByRemovedAtIsNull(pageable);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            HashMap<Long, List<HashMap<Long, String>>> tagList = getTagsForBoard(board.getIdx());
            List<TagDto> tagNames = tagRepository.findAllTags();
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, tagNames, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements());
    }

    public Page<BoardResponse> getBoardListWithTags(Pageable pageable, List<Long> tagIdx) throws BaseException {
        List<Long> boardIdxInBoardTag = findBoardIdxByAllTags(tagIdx);
        Page<Board> boardPage = boardRepository.findByIdxInAndRemovedAtIsNull(boardIdxInBoardTag, pageable);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            HashMap<Long, List<HashMap<Long, String>>> tagList = getTagsForBoard(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, tagNames, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements());
    }

    public List<Long> findBoardIdxByAllTags(List<Long> tagIdxList) {
        List<Board> boards = boardTagRepository.findBoardByTagIn(tagIdxList);
        List<Long> result = new ArrayList<>();

        for (Board board : boards) {
            List<Long> tagsForBoard = boardTagRepository.findTagIdxByBoardIdx(board.getIdx());
            if (new HashSet<>(tagsForBoard).containsAll(tagIdxList)) { result.add(board.getIdx()); }
        }
        return result;
    }

    public HashMap<Long, List<HashMap<Long, String>>> getTagsForBoard(Long boardIdx) throws BaseException {
        HashMap<Long, List<HashMap<Long, String>>> result = new HashMap<>();
        List<BoardTag> boardTags = boardTagRepository.findTagIdxListByBoardIdx(boardIdx);
        List<HashMap<Long, String>> tagsList = new ArrayList<>();
        for (BoardTag boardTag : boardTags) {
            Tag tag = boardTag.getTag();
            HashMap<Long, String> tagMap = new HashMap<>();
            tagMap.put(tag.getIdx(), tag.getTagName());
            tagsList.add(tagMap);
        }
        result.put(boardIdx, tagsList);
        return result;
    }

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

    public BoardMyPostResponse getMyPost(Long boardIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        if(board.isDeleted()) { throw new BaseException(BaseResponseStatus.POST_DELETED); }
        return new BoardMyPostResponse(board);
    }

    @Transactional
    public String deletePost(Long boardIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        board.removeBoard();
        return "게시글 삭제에 성공했습니다.";
    }

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
