package com.nbcamp.app.service;

import com.nbcamp.app.dto.BoardRequestDTO;
import com.nbcamp.app.dto.BoardResponseDTO;
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

    public BoardResponseDTO register(BoardRequestDTO board) {
        var newBoard = board.toEntity();
        boardRepository.save(newBoard);
        return new BoardResponseDTO(newBoard);
    }

    public BoardResponseDTO find(Long boardNumber) {
        Board foundboard = boardRepository.findById(boardNumber).orElseThrow();

        return new BoardResponseDTO(foundboard);
    }

    public List<BoardResponseDTO> findAll() {
       List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "registerDate"));

       return boardList.stream().map(e -> new BoardResponseDTO(e)).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public BoardResponseDTO modify(Long boardNumber, BoardRequestDTO board) {
        Board foundboard = findById(boardNumber);

        if(!(board.getBoardPassword().equals(foundboard.getBoardPassword()))){
            throw  new IllegalArgumentException("비밀번호가 틀립니다.");
        }
        foundboard.setBoardTitle(board.getBoardTitle());
        foundboard.setBoardContent(board.getBoardContent());
        foundboard.setBoardContent(board.getBoardContent());

        return new BoardResponseDTO(foundboard);
    }

    @Transactional
    public void delete(Long boardNumber) {

        boardRepository.delete(findById(boardNumber));
    }

    public Board findById(Long boardNumber) {
        return boardRepository.findById(boardNumber).orElseThrow();
    }
}
