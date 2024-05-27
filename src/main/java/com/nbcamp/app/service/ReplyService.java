package com.nbcamp.app.service;

import com.nbcamp.app.dto.ReplyRequestDto;
import com.nbcamp.app.dto.ReplyResponseDto;
import com.nbcamp.app.entity.Board;
import com.nbcamp.app.entity.Reply;
import com.nbcamp.app.entity.User;
import com.nbcamp.app.repository.BoardRepository;
import com.nbcamp.app.repository.ReplyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service 클래스를 Bean 컨테이너에 등록해주는 어노테이션
@Service
// 생성자 주입을 가능하게 해주는 어노테이션
@RequiredArgsConstructor
public class ReplyService {

    // DB 조작을 위한 Repository들과 토큰 검증에 필요한 JwtUtil을 생성자 주입
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;

    // 댓글 등록
    // DataAccessException을 통해 DB에 연결되지 못해 저장되지 않는 경우 예외 처리
    public ReplyResponseDto register(ReplyRequestDto replyRequestDto, Long boardNumber, HttpServletRequest request) throws DataAccessException {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 받아온 일정 ID로 댓글을 수정할 일정 찾아오는 메서드
        Board foundBoard = findBoard(boardNumber);

        // 입력받은 DTO를 통해 저장할 Reply 객체 생성 Board와 User와도 연관관계가 맺어져 있기 때문에 같이 전달
        var Reply = replyRequestDto.toEntity(foundBoard, foundUser);

        // 생성된 Reply를 DB에 저장
        replyRepository.save(Reply);

        // 리턴해주기 위한 response 객체 생성해서 리턴
        return new ReplyResponseDto(Reply, foundUser, foundBoard);

    }

    // 댓글 수정
    // 찾아온 객체들을 영속성 컨텍스트에 저장하기 위해 트랙잭션을 메서드 단위로 해주는 어노테이션
    @Transactional
    // DataAccessException을 통해 DB에 연결되지 못해 저장되지 않는 경우 예외 처리
    public ReplyResponseDto modify(ReplyRequestDto replyRequestDto, Long boardNumber, HttpServletRequest request) throws DataAccessException {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 받아온 일정 ID로 댓글을 수정할 일정 찾아오는 메서드
        Board foundBoard = findBoard(boardNumber);

        // 인덱스로 찾으려 했으나 중간에 삭제된 댓글이 있으면 인덱스로 찾을 수 없는 오류를 발견하여 stream방식으로 변경
        Reply foundReply = foundBoard.getReplyList().stream()
                // 입력받은 댓글 번호와 일치하는 댓글 찾기
                .filter(reply -> reply.getReplyNumber() == replyRequestDto.getReplyNumber()).findAny().orElseThrow(
                        // 일치하는 댓글이 없을 경우 예외 처리
                () -> new IllegalArgumentException("선택하신 댓글이 존재하지 않습니다!")
        );

        // 현재 수정중인 유저의 정보와 댓글의 유저 정보가 다를 경우 예외 처리
        if (!(foundUser == foundReply.getUser())) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 새로 입력받은 내용 중 내용만 setter를 통해 변경
        foundReply.setReplyContent(replyRequestDto.getReplyContent());

        // 변경된 내용을 DB에 저장
        replyRepository.save(foundReply);

        // 리턴해주기 위한 response 객체 생성해서 리턴
        return new ReplyResponseDto(foundReply, foundUser, foundBoard);

    }

    // 댓글 삭제
    // 찾아온 객체들을 영속성 컨텍스트에 저장하기 위해 트랙잭션을 메서드 단위로 해주는 어노테이션
    @Transactional
    public void delete(Long boardNumber, Long replyNumber, HttpServletRequest request) {
        // 받아온 토큰 검증 후 유저 정보를 받아오는 메서드
        User foundUser = userService.checktokenAndFindUser(request);

        // 받아온 일정 ID로 댓글을 수정할 일정 찾아오는 메서드
        Board foundBoard = findBoard(boardNumber);

        // 인덱스로 찾으려 했으나 중간에 삭제된 댓글이 있으면 인덱스로 찾을 수 없는 오류를 발견하여 stream방식으로 변경
        Reply foundReply = foundBoard.getReplyList().stream()
                // 입력받은 댓글 번호와 일치하는 댓글 찾기
                .filter(reply -> reply.getReplyNumber() == replyNumber).findAny().orElseThrow(
                        // 일치하는 댓글이 없을 경우 예외 처리
                        () -> new IllegalArgumentException("선택하신 댓글이 존재하지 않습니다!")
                );

        // 현재 삭제중인 유저의 정보와 댓글의 유저 정보가 다를 경우 예외 처리
        if (!(foundUser == foundReply.getUser())) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        // 찾은 댓글을 DB에서 삭제
        replyRepository.delete(foundReply);
    }

    // 받아온 일정 ID로 댓글을 CRUD 할 일정 찾아오는 메서드
    public Board findBoard(Long boardNumber) {
        // 받은 일정 ID로 DB에서 일정 찾아오기
        Board board = boardRepository.findById(boardNumber).orElseThrow(
                // 입력받은 일정 ID가 존재하지 않을 경우 예외 처리
                () -> new IllegalArgumentException("해당 일정은 존재하지 않습니다.")
        );
        // 찾은 일정 정보를 사용할 수 있도록 리턴
        return board;
    }
}
