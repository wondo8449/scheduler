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
    public void saveTest(){
        Board board = new Board();
        board.setBoardTitle("제목 테스트4");
        board.setBoardContent("내용 테스트4");
        board.setBoardWriter("김예찬");
        board.setBoardPassword("1234");

        Assertions.assertThat(boardRepository.save(board)).isEqualTo(board);
    }

    @Test
    public void findAllTest() {

    }

//    @Test
//    public void modifyTest() {
//        Board modifyboard = boardRepository.findById(6L);
//        modifyboard.setBoardContent("수정된 내용");
//        boardRepository.modify(modifyboard);
//    }
}
