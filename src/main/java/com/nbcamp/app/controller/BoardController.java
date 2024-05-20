package com.nbcamp.app.controller;

import com.nbcamp.app.dto.BoardRequestDTO;
import com.nbcamp.app.dto.BoardResponseDTO;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name= "일정 추가", description = "추가할 일정을 입력해주세요")
    @Parameters({
            @Parameter(name = "boardTitle", description = "일정 제목", example = "일정 제목을 적어주세요."),
            @Parameter(name = "boardContent", description = "일정 내용", example = "일정 내용을 적어주세요."),
            @Parameter(name = "boardWriter", description = "담당자", example = "김예찬"),
            @Parameter(name = "boardPassword", description = "비밀번호", example = "1234"),
    })
    public BoardResponseDTO write(@RequestBody BoardRequestDTO board) {
        return boardService.register(board);
    }

    @GetMapping("/read")
    public BoardResponseDTO read(@RequestParam Long boardNumber) throws NoSuchElementException {
        return boardService.find(boardNumber);
    }

    @GetMapping("/list")
    public List<BoardResponseDTO> getList() {
        return boardService.findAll();
    }

    @PutMapping("/modify")
    @Transactional
    public BoardResponseDTO modify(@RequestBody Board board) {



            return boardService.modify(board);
    }

    @DeleteMapping("/delete")
    @Transactional
    public String delete(@RequestBody Board board) {

        Board deleteBoard = boardService.findById(board.getBoardNumber());

        if(board.getBoardPassword().equals(deleteBoard.getBoardPassword())) {

            boardService.delete(deleteBoard.getBoardNumber());

            return "삭제를 성공하였습니다.";
        }
        return "비밀번호가 틀립니다.";
    }

    @ExceptionHandler
    public String handleNoSuchElementFoundException(NoSuchElementException exception) {
        return "해당 일정은 존재하지 않습니다.";
    }
}
