package com.nbcamp.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    public String write(@RequestBody Board board) {
        boardService.register(board);
        return board.toString();
    }

    @GetMapping("/read")
    public String read(Long boardNumber) {
        return boardService.findById(boardNumber).toString();
    }

    @GetMapping("/list")
    public List<Board> getList() {
        return boardService.findAll();
    }

    @PutMapping("/modify")
    @Transactional
    public String modify(@RequestBody ObjectNode saveObj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Board boardinput = mapper.treeToValue(saveObj.get("board"), Board.class);
        Board passwordinput = mapper.treeToValue(saveObj.get("password"), Board.class);

        Board modifyBoard = boardService.findById(boardinput.getBoardNumber());

        if(passwordinput.getBoardPassword().equals(modifyBoard.getBoardPassword())) {

            modifyBoard.setBoardTitle(boardinput.getBoardTitle());
            modifyBoard.setBoardContent(boardinput.getBoardContent());
            modifyBoard.setBoardWriter(boardinput.getBoardWriter());
            boardService.modify(modifyBoard);

            return "수정을 성공하였습니다.";
        }
        return "비밀번호가 틀립니다.";
    }

    @DeleteMapping("/delete")
    @Transactional
    public String delete(@RequestBody ObjectNode saveObj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Board boardinput = mapper.treeToValue(saveObj.get("board"), Board.class);
        Board passwordinput = mapper.treeToValue(saveObj.get("password"), Board.class);

        Board deleteBoard = boardService.findById(boardinput.getBoardNumber());

        if(passwordinput.getBoardPassword().equals(deleteBoard.getBoardPassword())) {

            boardService.delete(boardinput.getBoardNumber());

            return "삭제를 성공하였습니다.";
        }
        return "비밀번호가 틀립니다";
    }
}
