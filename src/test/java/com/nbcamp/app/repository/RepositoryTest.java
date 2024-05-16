package com.nbcamp.app.repository;

import com.nbcamp.app.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(false)
public class RepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    void saveTest(){
        Board board = new Board();
        board.setBoardTitle("레포지토리 테스트2");
        board.setBoardContent("테스트2");
        board.setBoardWriter("김예찬");
        board.setBoardPassword("2345");

        Assertions.assertThat(boardRepository.save(board)).isEqualTo(board);
    }

    @Test
    void findByIdTest() {
        log.info("일정 : " +boardRepository.findById(3L));
    }

    @Test
    void findAllTest() {
        log.info("일정 리스트 : " + boardRepository.findAll());
    }

    @Test
    void updateTest() {
        Board foundboard = boardRepository.findById(15L).orElseThrow();

        foundboard.setBoardNumber(15L);
        foundboard.setBoardTitle("레포지토리 수정 테스트");
        foundboard.setBoardContent("수정한 내용");

        boardRepository.save(foundboard);
    }

    @Test
    void deleteTest() {
        boardRepository.delete(boardRepository.findById(12L).orElseThrow());
    }
}
