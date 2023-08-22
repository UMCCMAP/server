package com.umc.cmap.domain.board.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.board.dto.*;
import com.umc.cmap.domain.board.entity.*;
import com.umc.cmap.domain.board.repository.*;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import com.umc.cmap.domain.comment.repository.CommentRepository;
import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import com.umc.cmap.domain.user.repository.UserRepository;
import com.umc.cmap.file.AwsS3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import static com.umc.cmap.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardTagRepository boardTagRepository;
    private final TagRepository tagRepository;
    private final CafeRepository cafeRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final ProfileRepository profileRepository;
    private final AuthService authService;
    private final BoardImageRepository boardImageRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;



    public BoardListResponse getBoardList(Pageable pageable) throws BaseException {
        Page<Board> boardPage = boardRepository.findAllByRemovedAtIsNull(pageable);
        Long cntBoard = boardRepository.countByRemovedAtIsNull();
        Long cntPage = (long) Math.ceil(cntBoard.doubleValue() / 5);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        List<TagDto> tagNames = tagRepository.findAllTags();
        return new BoardListResponse(new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements()), cntBoard, cntPage, tagNames);
    }

    public BoardListResponse getBoardListWithTags(Pageable pageable, List<Long> tagIdx) throws BaseException {
        List<Long> boardIdxInBoardTag = findBoardIdxByAllTags(tagIdx);
        Page<Board> boardPage = boardRepository.findByIdxInAndRemovedAtIsNull(boardIdxInBoardTag, pageable);
        Long cntBoard = boardRepository.countByIdxInAndRemovedAtIsNull(boardIdxInBoardTag);
        Long cntPage = (long) Math.ceil(cntBoard.doubleValue() / 5);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new BoardListResponse(new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements()),cntBoard, cntPage, tagNames);
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

    private List<HashMap<Long, String>> getTagsForBoard(Long boardIdx) throws BaseException {
        List<BoardTag> boardTags = boardTagRepository.findTagIdxListByBoardIdx(boardIdx);
        return boardTags.stream()
                .map(boardTag -> {
                    Tag tag = boardTag.getTag();
                    HashMap<Long, String> tagMap = new HashMap<>();
                    tagMap.put(tag.getIdx(), tag.getTagName());
                    return tagMap;
                })
                .collect(Collectors.toList());
    }

    private List<String> getImageUrlForMain(Long boardIdx) {
        List<BoardImage> images = boardImageRepository.findBoardImageListByBoardIdx(boardIdx);
        int maxImageCount = 3;
        if (images.size() > maxImageCount) {
            return images.stream()
                    .sorted(Comparator.comparing(BoardImage::getIdx))
                    .limit(maxImageCount)
                    .map(BoardImage::getImageUrl)
                    .collect(Collectors.toList());
        }
        return images.stream()
                .map(BoardImage::getImageUrl)
                .collect(Collectors.toList());
    }


    @Transactional
    public Long writeBoard(BoardWriteRequest request, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        Cafe cafe = cafeRepository.findById(request.getCafeIdx())
                .orElseThrow(() -> new BaseException(CAFE_NOT_FOUND));

        Board board = Board.builder()
                .user(user)
                .cafe(cafe)
                .boardTitle(request.getBoardTitle())
                .boardContent(request.getBoardContent())
                .role(Role.USER)
                .build();

        Board savedBoard = boardRepository.save(board);
        postBoardTagList(request.getTagList(), savedBoard);
        postBoardImgList(request.getImgList(), savedBoard);

        return savedBoard.getIdx();
    }

    private void postBoardTagList(List<Long> tagList, Board board) throws BaseException {
        for (Long tagIdx : tagList) {
            Tag tag = tagRepository.findById(tagIdx)
                    .orElseThrow(() -> new BaseException(TAG_NOT_FOUND));

            boardTagRepository.save(new BoardTag(board, tag));
        }
    }

    private void postBoardImgList(List<String> imgList, Board board) throws BaseException {
        for (String img : imgList) {
            boardImageRepository.save(new BoardImage(img, board));
        }
    }

    public BoardPostViewResponse getPostView(Long boardIdx, HttpServletRequest token) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        if(board.isDeleted()) { throw new BaseException(POST_DELETED); }
        boolean canModifyPost = checkUser(board.getUser().getIdx(),token);
        String profileImg = getProfileImg(board.getUser().getIdx());
        boolean like = likeBoardRepository.existsByBoardIdxAndUserIdx(boardIdx, authService.getUser(token).getIdx());
        Long cntLike = likeBoardRepository.countByBoardIdx(boardIdx);
        Long cntComment = commentRepository.countByUserIdx(board.getUser().getIdx());
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<HashMap<Long, String>> tagList = getTagsForBoard(boardIdx);
        return new BoardPostViewResponse(board, profileImg, cntLike, cntComment, tagList, tagNames, like, canModifyPost);
    }

    private String getProfileImg(Long userIdx) throws BaseException {
        Optional<Profile> profile = profileRepository.findByUserIdx(userIdx);
        return profile.get().getUserImg();
    }

    @Transactional
    public String deletePost(Long boardIdx, HttpServletRequest token) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        if (!checkUser(board.getUser().getIdx(), token)) { throw new BaseException(DONT_HAVE_ACCESS); }
        board.removeBoard();
        return "게시글 삭제에 성공했습니다.";
    }

    @Transactional
    public String modifyPost(Long boardIdx, BoardModifyRequest request, HttpServletRequest token) throws BaseException {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        if (!checkUser(board.getUser().getIdx(),token)) { throw new BaseException(DONT_HAVE_ACCESS); }
        Cafe cafe = cafeRepository.findById(request.getCafeIdx())
                .orElseThrow(() -> new BaseException(CAFE_NOT_FOUND));
        modifyTagList(request.getTagList(), board);
        modifyImgList(request.getImgList(), board);
        board.modifyPost(cafe, request.getBoardTitle(), request.getBoardContent());

        return "게시글 수정에 성공했습니다.";
    }

    private boolean checkUser(Long writer, HttpServletRequest token) throws BaseException {
        return writer.equals(authService.getUser(token).getIdx());
    }

    private void modifyImgList(List<String> imgList, Board board) throws BaseException {
        List<BoardImage> existingImgs = boardImageRepository.findBoardImageListByBoardIdx(board.getIdx());
        for (String imgUrl : imgList) {
            if (!containsImg(existingImgs, imgUrl)) {
                boardImageRepository.save(new BoardImage(imgUrl, board));
            }
        }
        for (BoardImage existingImg : existingImgs) {
            if(!imgList.contains(existingImg.getImageUrl())) {
                boardImageRepository.delete(existingImg);
                awsS3Service.deleteFile(existingImg.getImageUrl());
            }
        }
    }

    private boolean containsImg(List<BoardImage> boardImages, String imgUrl) {
        for (BoardImage img : boardImages) {
            if (img.getImageUrl().equals(imgUrl)) { return true; }
        }
        return false;
    }

    private void modifyTagList(List<Long> tagList, Board board) throws BaseException {
        List<BoardTag> existingTags = boardTagRepository.findTagIdxListByBoardIdx(board.getIdx());
        for (Long tagIdx : tagList) {
            Tag tag = tagRepository.findById(tagIdx)
                    .orElseThrow(() -> new BaseException(TAG_NOT_FOUND));
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
    public String likePost(Long boardIdx, HttpServletRequest token) throws BaseException {
        Board board = boardRepository.findById(boardIdx).orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        User user = authService.getUser(token);
        if (likeBoardRepository.existsByBoardIdxAndUserIdx(boardIdx,user.getIdx())) {
            throw new BaseException(LIKE_BOARD_EXIST_DATA);
        }
        LikeBoard likeBoard = LikeBoard.builder()
                .board(board)
                .user(user)
                .build();
        likeBoardRepository.save(likeBoard);
        return "좋아요 성공";
    }

    @Transactional
    public String likePostCancel(Long boardIdx, HttpServletRequest token) throws BaseException {
        Long userIdx = authService.getUser(token).getIdx();
        LikeBoard likeBoard = likeBoardRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx)
                        .orElseThrow(() -> new BaseException(LIKE_BOARD_EXIST_DATA));
        likeBoardRepository.delete(likeBoard);
        return "좋아요 취소";
    }

    public BoardListResponse getBoardBySearchOfTitle(Pageable pageable, String keyword) throws BaseException {
        Page<Board> boardPage = boardRepository.findByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(keyword, keyword, pageable);
        Long cntBoard = boardRepository.countByBoardTitleContainingOrBoardContentContainingAndRemovedAtIsNull(keyword, keyword);
        Long cntPage = (long) Math.ceil(cntBoard.doubleValue() / 5);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new BoardListResponse(new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements()), cntBoard, cntPage, tagNames);
    }

    public BoardListResponse getBoardByWriter(Pageable pageable, String nickName) throws BaseException {
        User user = userRepository.findByNickname(nickName)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        Page<Board> boardPage = boardRepository.findByUserAndRemovedAtIsNull(user, pageable);
        Long cntBoard = boardRepository.countByUserAndRemovedAtIsNull(user);
        Long cntPage = (long) Math.ceil(cntBoard.doubleValue() / 5);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new BoardListResponse(new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements()), cntBoard, cntPage, tagNames);
    }

    public BoardListResponse getBoardByCafe(Pageable pageable, String cafeName) throws BaseException {
        List<Cafe> cafes = cafeRepository.findByNameContaining(cafeName);
        Page<Board> boardPage = boardRepository.findByCafeInAndRemovedAtIsNull(cafes, pageable);
        Long cntBoard = boardRepository.countByCafeInAndRemovedAtIsNull(cafes);
        Long cntPage = (long) Math.ceil(cntBoard.doubleValue() / 5);
        List<TagDto> tagNames = tagRepository.findAllTags();
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new BoardListResponse(new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements()), cntBoard, cntPage, tagNames);
    }

    public Page<BoardResponse> getMyBoardList(Pageable pageable, HttpServletRequest token) throws BaseException {
        User user = authService.getUser(token);
        Page<Board> boardPage = boardRepository.findByUserAndRemovedAtIsNull(user, pageable);
        List<BoardResponse> boardResponses = new ArrayList<>();
        for (Board board : boardPage) {
            List<HashMap<Long, String>> tagList = getTagsForBoard(board.getIdx());
            List<String> imgList = getImageUrlForMain(board.getIdx());
            BoardResponse boardResponse = new BoardResponse(board.getIdx(), board.getBoardTitle(), board.getBoardContent(), tagList, imgList, board.getCreatedAt());
            boardResponses.add(boardResponse);
        }
        return new PageImpl<>(boardResponses, pageable, boardPage.getTotalElements());
    }
}
