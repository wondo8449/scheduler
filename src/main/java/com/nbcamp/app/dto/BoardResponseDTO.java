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
        this.boardWriter = board.getUser().getUserName();
        this.registerDate = board.getRegisterDate();
    }
}
