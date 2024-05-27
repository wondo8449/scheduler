package com.nbcamp.app.dto;

import com.nbcamp.app.entity.Board;
import com.nbcamp.app.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {

    @NotEmpty(message = "일정 제목이 비어있습니다.")
    private String boardTitle;

    @NotEmpty(message = "일정 내용이 비어있습니다.")
    private String boardContent;

    @NotEmpty(message = "일정 비밀번호가 비어있습니다.")
    private String boardPassword;

    public Board toEntity(User user) {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardPassword(boardPassword)
                .user(user)
                .build();
    }
}
