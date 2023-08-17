package com.umc.cmap.domain.comment.service;


import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.comment.dto.CommentRequest;
import com.umc.cmap.domain.comment.dto.CommentResponse;
import com.umc.cmap.domain.comment.dto.UpdateCommentRequest;
import com.umc.cmap.domain.comment.entity.Comment;
import com.umc.cmap.domain.comment.mapper.CommentMapper;
import com.umc.cmap.domain.comment.repository.CommentRepository;
import com.umc.cmap.domain.user.entity.Profile;

import com.umc.cmap.domain.user.entity.User;
import com.umc.cmap.domain.user.login.service.AuthService;
import com.umc.cmap.domain.user.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CommentService {
    CommentRepository commentRepository;
    BoardRepository boardRepository;
    ProfileRepository profileRepository;
    CommentMapper mapper;
    AuthService authService;

    public List<CommentResponse> getAll(Long boardIdx, Pageable pageable) {
        List<CommentResponse> comments = commentRepository.findAllByBoardIdx(boardIdx, pageable).stream().filter(c -> c.getRemovedAt() == null)
                .map(c -> mapper.toResponse(c, c.getUser())).toList();
        return setCommentUserImg(comments);
    }

    public List<CommentResponse> getAllByUser(User user, Pageable pageable) throws BaseException {
        List<CommentResponse> comments = commentRepository.findAllByUserIdx(user.getIdx(), pageable)
                .stream().map(c -> mapper.toResponse(c, user)).toList();
        return setCommentUserImg(comments);
    }

    private List<CommentResponse> setCommentUserImg(List<CommentResponse> comments) {
        comments.forEach(c -> c.setUserImg(getWriterProfileImg(c.getUserIdx())));
        return comments;
    }

    private String getWriterProfileImg(Long userIdx) {
        Profile profile = profileRepository.findByUserIdx(userIdx).orElseThrow(EntityNotFoundException::new);
        return profile.getUserImg();
    }

    @Transactional
    public void save(Long boardIdx, CommentRequest param, HttpServletRequest request) throws BaseException {
        param.setBoard(boardRepository.findById(boardIdx).orElseThrow(EntityNotFoundException::new));
        commentRepository.save(mapper.toEntity(param, getLoginUser(request)));
    }

    private User getLoginUser(HttpServletRequest request) throws BaseException {
        return authService.getUser(request);
    }

    @Transactional
    public void update(Long commentIdx, UpdateCommentRequest param) {
        getEntity(commentIdx).update(param.getContent());

    }

    private Comment getEntity(Long commentIdx) {
        return commentRepository.findById(commentIdx).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void delete(Long commentIdx) {
        getEntity(commentIdx).delete();
    }

}
