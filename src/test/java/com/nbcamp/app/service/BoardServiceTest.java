package com.nbcamp.app.service;

import lombok.extern.slf4j.Slf4j;
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

//    @Test
//    void registerTest() {
//        BoardR board1 = new Board();
//        board1.setBoardTitle("Service 테스트");
//        board1.setBoardContent("Service Test 내용");
//        board1.setBoardWriter("김예찬");
//        board1.setBoardPassword("5678");
//
//        log.info("등록된 일정 : " + boardService.register(board1));
//    }

//    @Test
//    void findTest() {
//        log.info(boardService.findById(10L).toString());
//    }
//
//    @Test
//    void findAllTest() {
//        log.info("리스트 : " + boardService.findAll());
//    }

//    @Test
//    void modifyTest() {
//        BoardRequestDTO board2 = new BoardRequestDTO();
//        board2.setBoardTitle("Service 테스트2");
//        board2.setBoardContent("Service Test 내용");
//        board2.setBoardWriter("김예찬");
//        board2.setBoardPassword("1234");
//
//        log.info("수정된 일정 : " + boardService.modify(9L, board2));
//    }

//    @Test
//    void deleteTest() {
//        boardService.delete(7L);
//    }
}
