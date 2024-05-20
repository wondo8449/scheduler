package com.nbcamp.app.dto;

import com.nbcamp.app.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDTO {

    private Long boardNumber;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private String registerDate;

    public BoardResponseDTO(Board board) {
        this.boardNumber = board.getBoardNumber();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardWriter = board.getBoardWriter();
        this.registerDate = board.getRegisterDate();
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "boardNumber=" + boardNumber +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardContent='" + boardContent + '\'' +
                ", boardWriter='" + boardWriter + '\'' +
                ", registerDate='" + registerDate + '\'' +
                '}';
    }
}
