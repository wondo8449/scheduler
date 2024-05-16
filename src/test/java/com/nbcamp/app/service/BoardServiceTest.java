package com.nbcamp.app.service;

import com.nbcamp.app.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(value = false)
public class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Test
    void registerTest() {
        Board board1 = new Board();
        board1.setBoardTitle("Service 테스트");
        board1.setBoardContent("Service Test 내용");
        board1.setBoardWriter("김예찬");
        board1.setBoardPassword("5678");

        boardService.register(board1);
    }

    @Test
    void findTest() {
        log.info(boardService.findById(10L).toString());
    }

    @Test
    void findAllTest() {
        log.info("리스트 : " + boardService.findAll());
    }
}
