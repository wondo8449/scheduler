package com.nbcamp.app.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
public class EntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void boardTest() {
        Board board = new Board();
        board.setBoardTitle("제목 테스트1");
        board.setBoardContent("내용 테스트1");
        board.setBoardWriter("김예찬");
        board.setBoardPassword("1234");


        // 영속성 컨텍스트에 등록, 1차 캐시에 저장
        entityManager.persist(board);

        // 쓰기 지연 저장소의 SQL문을 모두 즉시 실행
        //entityManager.flush();
        // 영속성 컨텍스트 모두 비우기, 1차 캐시도 비워진다
        //entityManager.clear();

        // 1차 캐시 검사 후 동일한 주소가 있을 때에는 SQL문이 실행되지 않지만
        // 동일한 주소가 없을 경우 SQL문이 실행된다
        Board foundboard = entityManager.find(Board.class, 1L);

        log.info("board : " + board);
        log.info("foundboard : " + foundboard);

        assertThat(board).isEqualTo(foundboard);
    }

    @Test
    public void updateTest() {
        Board foundBoard = entityManager.find(Board.class, 1L);
        // 변경감지(더티체킹)
        foundBoard.setBoardWriter("김예찬2");
    }

    @Test
    public void deleteTest() {
        // 삭제할 때에는 영속 상태인 객체만 가능하다.
        entityManager.remove(entityManager.find(Board.class, 1L));
    }
}
