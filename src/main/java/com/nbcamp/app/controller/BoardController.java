package com.nbcamp.app.controller;

import com.nbcamp.app.dto.BoardRequestDto;
import com.nbcamp.app.dto.BoardResponseDto;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    @Tag(name= "일정 추가", description = "일정을 추가합니다.")
    public ResponseEntity write(@RequestBody @Valid BoardRequestDto board, HttpServletRequest request) {
        return ResponseEntity.ok().body(boardService.register(board, request));
    }

    @GetMapping("/")
    @Tag(name= "일정 조회", description = "일정을 조회합니다.")
    @Parameters({ @Parameter(name = "boardNumber", description = "조회하실 일정 번호를 적어주세요", example = "1")})
    public ResponseEntity read(@RequestParam Long boardNumber) {
        return ResponseEntity.ok().body(boardService.find(boardNumber));
    }

    @GetMapping("/list")
    @Tag(name= "전체 일정 조회", description = "전체 일정을 조회합니다.")
    public ResponseEntity<List<BoardResponseDto>> getList() {
        return ResponseEntity.ok().body(boardService.findAll());
    }

    @PutMapping("/{boardNumber}")
    @Tag(name= "일정 수정", description = "일정을 수정합니다.")
    @Parameters({ @Parameter(name = "boardNumber", description = "수정하실 일정 번호를 적어주세요", example = "1")})
    @Transactional
    public ResponseEntity modify(@PathVariable Long boardNumber, @RequestBody @Valid BoardRequestDto board, HttpServletRequest request) {
        return ResponseEntity.ok().body(boardService.modify(boardNumber, board, request));
    }

    @DeleteMapping("/delete")
    @Tag(name= "일정 삭제", description = "일정을 삭제합니다.")
    @Transactional
    public ResponseEntity delete(@RequestBody Board board, HttpServletRequest request) {

        return ResponseEntity.ok().body(boardService.delete(board, request));
    }
}
