package com.nbcamp.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nbcamp.app.dto.BoardDTO;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    public BoardDTO write(@RequestBody Board board) {
        return boardService.register(board);
    }

    @GetMapping("/read")
    public BoardDTO read(Long boardNumber) throws NoSuchElementException {
        return boardService.find(boardNumber);
    }

    @GetMapping("/list")
    public List<BoardDTO> getList() {
        return boardService.findAll();
    }

    @PutMapping("/modify")
    @Transactional
    public Object modify(@RequestBody Board board) {

        Board modifyBoard = boardService.findById(board.getBoardNumber());

        if(board.getBoardPassword().equals(modifyBoard.getBoardPassword())) {

            modifyBoard.setBoardTitle(board.getBoardTitle());
            modifyBoard.setBoardContent(board.getBoardContent());
            modifyBoard.setBoardWriter(board.getBoardWriter());

            return boardService.modify(modifyBoard);
        }
        return "비밀번호가 틀립니다.";
    }

    @DeleteMapping("/delete")
    @Transactional
    public String delete(Long boardNumber, String password) {

        Board deleteBoard = boardService.findById(boardNumber);

        if(password.equals(deleteBoard.getBoardPassword())) {

            boardService.delete(boardNumber);

            return "삭제를 성공하였습니다.";
        }
        return "비밀번호가 틀립니다.";
    }

    @ExceptionHandler
    public String handleNoSuchElementFoundException(NoSuchElementException exception) {
        return "해당 일정은 존재하지 않습니다.";
    }
}
