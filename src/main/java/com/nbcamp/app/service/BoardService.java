package com.nbcamp.app.service;

import com.nbcamp.app.dto.BoardDTO;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDTO register(Board board) {
        boardRepository.save(board);
        return new BoardDTO(board);
    }

    public BoardDTO find(Long boardNumber) {
        Board foundboard = boardRepository.findById(boardNumber).orElseThrow();

        return new BoardDTO(foundboard);
    }

    public List<BoardDTO> findAll() {
       List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "registerDate"));

       return boardList.stream().map(e -> new BoardDTO(e)).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public BoardDTO modify(Board board) {
        Board foundboard = findById(board.getBoardNumber());

        foundboard.setBoardTitle(board.getBoardTitle());
        foundboard.setBoardContent(board.getBoardContent());
        foundboard.setBoardContent(board.getBoardContent());

        return new BoardDTO(foundboard);
    }

    @Transactional
    public void delete(Long boardNumber) {
        boardRepository.delete(findById(boardNumber));
    }

    public Board findById(Long boardNumber) {
        return boardRepository.findById(boardNumber).orElseThrow();
    }
}
