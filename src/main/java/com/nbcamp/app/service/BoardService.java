package com.nbcamp.app.service;

import com.nbcamp.app.dto.BoardRequestDto;
import com.nbcamp.app.dto.BoardResponseDto;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.entity.User;
import com.nbcamp.app.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    // 일정 등록
    public BoardResponseDto register(BoardRequestDto board, HttpServletRequest request) {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 입력받은 DTO를 통해 저장할 Board 객체 생성 User와 연관관계가 있기 때문에 User도 전달
        var newBoard = board.toEntity(foundUser);

        // 생성된 Board를 DB에 저장
        boardRepository.save(newBoard);

        // 리턴해주기 위한 response 객체 생성해서 리턴
        return new BoardResponseDto(newBoard);
    }

    // 조회는 누구나 가능하게 하기 위해 기존과 동일
    public BoardResponseDto find(Long boardNumber) {
        Board foundboard = boardRepository.findById(boardNumber).orElseThrow();

        return new BoardResponseDto(foundboard);
    }

    // 조회는 누구나 가능하게 하기 위해 기존과 동일
    public List<BoardResponseDto> findAll() {
       List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "registerDate"));

       return boardList.stream().map(e -> new BoardResponseDto(e)).collect(Collectors.toCollection(ArrayList::new));
    }

    // 일정 수정
    @Transactional
    public BoardResponseDto modify(Long boardNumber, BoardRequestDto board, HttpServletRequest request) {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 수정할 Board를 가져온다
        Board foundboard = boardRepository.findById(boardNumber).orElseThrow(
                // 입력받은 일정 번호가 존재하지 않을 경우 예외 처리
                () -> new IllegalArgumentException("입력하신 일정이 존재하지 않습니다.")
        );

        // 현재 수정중인 유저의 정보와 댓글의 유저 정보가 다를 경우 예외 처리
        if (!(foundUser == foundboard.getUser())) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 작성 시 입력한 비밀번호가 다를 경우 예외 처리
        if(!(board.getBoardPassword().equals(foundboard.getBoardPassword()))){
            throw  new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        // 모든 검증이 완료되면 입력받은 수정 내용들을 set
        foundboard.setBoardTitle(board.getBoardTitle());
        foundboard.setBoardContent(board.getBoardContent());
        foundboard.setBoardContent(board.getBoardContent());

        // 수정된 내용을 DB에 저장(변경감지)
        boardRepository.save(foundboard);

        // response 객체를 통하여 리턴
        return new BoardResponseDto(foundboard);
    }

    // 일정 삭제
    @Transactional
    public String delete(Board board, HttpServletRequest request) {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 수정할 Board를 가져온다
        Board foundboard = boardRepository.findById(board.getBoardNumber()).orElseThrow(
                // 입력받은 일정 번호가 존재하지 않을 경우 예외 처리
                () -> new IllegalArgumentException("입력하신 일정이 존재하지 않습니다.")
        );

        // 현재 수정중인 유저의 정보와 댓글의 유저 정보가 다를 경우 예외 처리
        if (!(foundUser == foundboard.getUser())) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // DB에서 삭제
        boardRepository.delete(foundboard);

        // 삭제 결과를 리턴
        return "삭제에 성공하였습니다";
    }
}
