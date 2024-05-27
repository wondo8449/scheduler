package com.nbcamp.app.dto;

import com.nbcamp.app.entity.Board;
import com.nbcamp.app.entity.Reply;
import com.nbcamp.app.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequestDto {

    // Contoller단에서 Valid를 통해 NotEmpty를 달고 메세지를 담아둔다
    @NotEmpty(message = "댓글 내용이 비어있습니다.")
    private String replyContent;

    // 수정을 할 때만 조회를 위해서 Number를 입력받는다
    private Long replyNumber;

    // 받아온 값들을 Reply 객체로 build하기 위한 메서드
    // 연관관계가 맺어져 있기 때문에 Board와 User도 받아와준다
    public Reply toEntity(Board board, User user) {
        return Reply.builder()
                .replyContent(replyContent)
                .user(user)
                .board(board)
                .build();
    }
}
