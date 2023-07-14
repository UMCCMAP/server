package com.umc.cmap.domain.board.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {
    private final BoardService boardService;

}
