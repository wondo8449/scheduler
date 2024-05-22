package com.nbcamp.app.controller;

import com.nbcamp.app.dto.BoardRequestDTO;
import com.nbcamp.app.dto.BoardResponseDTO;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Tag(name= "일정 추가", description = "일정을 추가합니다.")
    public ResponseEntity write(@RequestBody BoardRequestDTO board) {
        return ResponseEntity.ok().body(boardService.register(board));
    }

    @GetMapping("/")
    @Tag(name= "일정 조회", description = "일정을 조회합니다.")
    @Parameters({ @Parameter(name = "boardNumber", description = "조회하실 일정 번호를 적어주세요", example = "1")})
    public ResponseEntity read(@RequestParam Long boardNumber) {
        return ResponseEntity.ok().body(boardService.find(boardNumber));
    }

    @GetMapping("/list")
    @Tag(name= "전체 일정 조회", description = "전체 일정을 조회합니다.")
    public ResponseEntity<List<BoardResponseDTO>> getList() {
        return ResponseEntity.ok().body(boardService.findAll());
    }

    @PutMapping("/{boardNumber}")
    @Tag(name= "일정 수정", description = "일정을 수정합니다.")
    @Parameters({ @Parameter(name = "boardNumber", description = "수정하실 일정 번호를 적어주세요", example = "1")})
    @Transactional
    public ResponseEntity modify(@PathVariable Long boardNumber, @RequestBody BoardRequestDTO board) {
        return ResponseEntity.ok().body(boardService.modify(boardNumber, board));
    }

    @DeleteMapping("/delete")
    @Tag(name= "일정 삭제", description = "일정을 삭제합니다.")
    @Transactional
    public ResponseEntity delete(@RequestBody Board board) {

        Board deleteBoard = boardService.findById(board.getBoardNumber());

        if(board.getBoardPassword().equals(deleteBoard.getBoardPassword())) {

            boardService.delete(deleteBoard.getBoardNumber());

            return ResponseEntity.ok().body("삭제를 성공하였습니다.");
        }
        return ResponseEntity.ok().body("비밀번호가 틀립니다.");
    }

    @ExceptionHandler
    public String handleNoSuchElementFoundException(NoSuchElementException exception) {
        return "해당 일정은 존재하지 않습니다.";
    }
}
