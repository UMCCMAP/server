package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.board.dto.*;
import com.umc.cmap.domain.board.entity.*;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.board.repository.BoardTagRepository;
import com.umc.cmap.domain.board.repository.LikeBoardRepository;
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
    private final LikeBoardRepository likeBoardRepository;


    public BoardListResponse getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAllByRemovedAtIsNull(pageable);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            HashMap<Long, List<HashMap<Long, String>>> tagList = getTagsForBoard(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        List<TagDto> tagNames = tagRepository.findAllTags();
        Page<BoardResponse> pagedBoardResponses = new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements());
        return new BoardListResponse(pagedBoardResponses, tagNames);
    }

    public BoardListResponse getBoardListWithTags(Pageable pageable, List<Long> tagIdx) throws BaseException {
        List<Long> boardIdxInBoardTag = findBoardIdxByAllTags(tagIdx);
        Page<Board> boardPage = boardRepository.findByIdxInAndRemovedAtIsNull(boardIdxInBoardTag, pageable);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            HashMap<Long, List<HashMap<Long, String>>> tagList = getTagsForBoard(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        Page<BoardResponse> pagedBoardResponses = new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements());
        return new BoardListResponse(pagedBoardResponses, tagNames);
    }

    private List<Long> findBoardIdxByAllTags(List<Long> tagIdxList) {
        List<Board> boards = boardTagRepository.findBoardByTagIn(tagIdxList);
        List<Long> result = new ArrayList<>();
        for (Board board : boards) {
            List<Long> tagsForBoard = boardTagRepository.findTagIdxByBoardIdx(board.getIdx());
            if (new HashSet<>(tagsForBoard).containsAll(tagIdxList)) { result.add(board.getIdx()); }
        }
        return result;
    }

    private HashMap<Long, List<HashMap<Long, String>>> getTagsForBoard(Long boardIdx) throws BaseException {
        List<BoardTag> boardTags = boardTagRepository.findTagIdxListByBoardIdx(boardIdx);
        List<HashMap<Long, String>> tagsList = boardTags.stream()
                .map(boardTag -> {
                    Tag tag = boardTag.getTag();
                    HashMap<Long, String> tagMap = new HashMap<>();
                    tagMap.put(tag.getIdx(), tag.getTagName());
                    return tagMap;
                })
                .collect(Collectors.toList());
        HashMap<Long, List<HashMap<Long, String>>> result = new HashMap<>();
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
        postBoardTagList(request.getTagList(), savedBoard);

        return savedBoard.getIdx();
    }

    private void postBoardTagList(List<Long> tagList, Board board) throws BaseException {
        for (Long tagIdx : tagList) {
            Tag tag = tagRepository.findById(tagIdx)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));

            boardTagRepository.save(new BoardTag(board, tag));
        }
    }

    public BoardMyPostResponse getMyPost(Long boardIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        if(board.isDeleted()) { throw new BaseException(BaseResponseStatus.POST_DELETED); }
        HashMap<Long, List<HashMap<Long, String>>> tagList = getTagsForBoard(board.getIdx());
        return new BoardMyPostResponse(board, tagList);
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
        modifyTagList(request.getTagList(), board);
        board.modifyPost(cafe, request.getBoardTitle(), request.getBoardContent());

        return "게시글 수정에 성공했습니다.";
    }

    private void modifyTagList(List<Long> tagList, Board board) throws BaseException {
        List<BoardTag> existingTags = boardTagRepository.findTagIdxListByBoardIdx(board.getIdx());
        for (Long tagIdx : tagList) {
            Tag tag = tagRepository.findById(tagIdx)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));
            if (!containsTag(existingTags, tagIdx)) {
                boardTagRepository.save(new BoardTag(board, tag));
            }
        }
        for (BoardTag existingTag : existingTags) {
            if (!tagList.contains(existingTag.getTag().getIdx())) {
                boardTagRepository.delete((existingTag));
            }
        }
    }

    private boolean containsTag(List<BoardTag> tags, Long tagIdx) {
        for (BoardTag tag : tags) {
            if (tag.getTag().getIdx().equals(tagIdx)) { return true; }
        }
        return false;
    }

    @Transactional
    public String likePost(Long boardIdx, Long userIdx) throws BaseException {
        Board board = boardRepository.findById(boardIdx).orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        LikeBoard likeBoard = LikeBoard.builder()
                .board(board)
                .user(user)
                .build();
        likeBoardRepository.save(likeBoard);
        return "좋아요 성공";
    }

    @Transactional
    public String likePostCancel(Long boardIdx, Long userIdx) throws BaseException {
        LikeBoard likeBoard = likeBoardRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx);
        likeBoardRepository.delete(likeBoard);
        return "좋아요 취소";
    }

    public BoardListResponse getBoardBySearch(Pageable pageable, String keyword) throws BaseException {
        Page<Board> boardList = boardRepository.findByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(keyword, keyword, pageable);
    }

}
