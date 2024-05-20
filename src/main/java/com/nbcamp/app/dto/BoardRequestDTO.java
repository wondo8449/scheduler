package com.nbcamp.app.dto;

import com.nbcamp.app.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private String boardPassword;

    public Board toEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardWriter(boardWriter)
                .boardPassword(boardPassword)
                .build();
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardContent='" + boardContent + '\'' +
                ", boardWriter='" + boardWriter + '\'' +
                '}';
    }
}
