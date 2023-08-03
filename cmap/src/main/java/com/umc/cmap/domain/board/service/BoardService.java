package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
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
        // 고칠 부분
        HashMap<Long,List<HashMap<Long,String>>> tagList = getTagsForBoardList(boardPage);
        List<TagDto> tagNames = tagRepository.findAllTags();
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, tagNames, board.getCreatedAt()));
    }

    /**
     * 게시판 메인 화면 (특정 태그 선택 O)
     * @param pageable
     * @param tagIdx
     * @return
     * @throws BaseException
     */
    public Page<BoardResponse> getBoardListWithTags(Pageable pageable, List<Long> tagIdx) throws BaseException {
        List<Tag> tags = tagRepository.findAllByIdxIn(tagIdx);
        List<BoardTag> boardTags = boardTagRepository.findBoardIdxByTagIn(tags);
        List<Long> boardIdx = boardTags.stream()
                .map(boardTag -> boardTag.getBoard().getIdx())
                .collect(Collectors.toList());
        Page<Board> boardPage = boardRepository.findByIdxInAndRemovedAtIsNull(boardIdx, pageable);
        // 고칠 부분
        HashMap<Long,List<HashMap<Long,String>>> tagList = getTagsForBoardList(boardPage);
        List<TagDto> tagNames = tagRepository.findAllTags();
        return boardPage.map(board -> new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, tagNames, board.getCreatedAt()));
    }

    /**
     * 태그들 가져오는 코드 - 해당 boardIdx에 연관된 tagIdx를 받아오고, 이를 이용해서 tagName까지 리턴
     * 게시글 불러올 때 사용할 예정
     */
    public HashMap<Long, List<HashMap<Long, String>>> getTagsForBoardList(Page<Board> boardPage) throws BaseException {
        HashMap<Long, List<HashMap<Long, String>>> result = new HashMap<>();
        List<Board> boards = boardPage.getContent();
        for (Board board : boards) {
            List<BoardTag> BoardTags = boardTagRepository.findTagIdxListByBoardIdx(board.getIdx());
            List<Long> tagIdxList = BoardTags.stream()
                    .map(boardTag -> boardTag.getTag().getIdx())
                    .collect(Collectors.toList());
            List<HashMap<Long,String>> tags = new ArrayList<>();
            for (Long tagIdx : tagIdxList) {
                Tag tag = tagRepository.findById(tagIdx)
                        .orElseThrow(()-> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));
                HashMap<Long, String> tagMap = new HashMap<>();
                tagMap.put(tag.getIdx(), tag.getTagName());
                tags.add(tagMap);
            }
            result.put(board.getIdx(), tags);
        }
        return result;
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
