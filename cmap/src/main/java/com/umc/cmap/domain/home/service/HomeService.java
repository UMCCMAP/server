package com.umc.cmap.domain.home.service;

import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.board.entity.BoardImage;
import com.umc.cmap.domain.board.entity.Role;
import com.umc.cmap.domain.board.repository.BoardImageRepository;
import com.umc.cmap.domain.board.repository.BoardRepository;
import com.umc.cmap.domain.home.dto.HomeResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HomeService {
    BoardRepository boardRepository;
    BoardImageRepository boardImgRepository;

    public List<HomeResponse> get(Pageable pageable) {
        List<Board> boards = boardRepository.findTop2ByRoleLike(Role.ADMIN, pageable);
        return boards.stream().map(this::toResponse).toList();
    }

    private HomeResponse toResponse(Board board) {
        log.info("[home] Board Id: {}", board.getIdx());
        return HomeResponse.builder()
                .boardIdx(board.getIdx())
                .imageUrl(getImgUrl(board))
                .build();
    }

    private String getImgUrl(Board board) {
        BoardImage image = boardImgRepository.findTop1ByBoard(board).orElse(null);
        if (image == null) {
            return null;
        }
        return image.getImageUrl();
    }
}
