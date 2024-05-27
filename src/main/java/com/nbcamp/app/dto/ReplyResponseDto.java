package com.nbcamp.app.dto;

import com.nbcamp.app.entity.Board;
import com.nbcamp.app.entity.Reply;
import com.nbcamp.app.entity.User;
import lombok.Getter;

@Getter
// Reply 정보를 리턴해주기 위한 객체
public class ReplyResponseDto {

    private Long replyNumber;
    private String replyContent;
    private Long userNumber;
    private Long boardNumber;
    private String registerDate;

    public ReplyResponseDto(Reply reply, User user, Board board) {
        this.replyNumber = reply.getReplyNumber();
        this.replyContent = reply.getReplyContent();
        this.userNumber = user.getUserNumber();
        this.boardNumber = board.getBoardNumber();
        this.registerDate = reply.getRegisterDate();
    }
}
