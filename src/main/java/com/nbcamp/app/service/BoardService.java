package com.nbcamp.app.service;

import com.nbcamp.app.entity.Board;
import com.nbcamp.app.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board register(Board board) {
        boardRepository.save(board);
        return board;
    }

    public Board findById(Long boardNumber) {
        return boardRepository.findById(boardNumber).orElseThrow(() ->
                new IllegalArgumentException("선택한 일정은 존재하지 않습니다.")
        );
    }

    public List<Board> findAll() {
       return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "registerDate"));
    }

    @Transactional
    public Board modify(Board board) {
        Board foundboard = findById(board.getBoardNumber());

        foundboard.setBoardTitle(board.getBoardTitle());
        foundboard.setBoardContent(board.getBoardContent());
        foundboard.setBoardContent(board.getBoardContent());

        return foundboard;
    }

    @Transactional
    public void delete(Long boardNumber) {
        boardRepository.delete(findById(boardNumber));
    }
}
