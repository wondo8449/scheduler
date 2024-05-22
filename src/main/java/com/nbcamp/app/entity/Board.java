package com.nbcamp.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Board extends DateTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNumber;
    @Column
    private String boardTitle;
    @Column
    private String boardContent;
    @Column
    private String boardWriter;
    @Column(nullable = false)
    private String boardPassword;

    @Builder
    public Board(String boardTitle, String boardContent, String boardWriter, String boardPassword) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardWriter = boardWriter;
        this.boardPassword = boardPassword;
    }


    @Override
    public String toString() {
        return "번호 : " + boardNumber + ",\n 제목 : " + boardTitle + ",\n 내용 : " + boardContent + ",\n 담당자 : "
                + boardWriter + ",\n 작성일자 : " + getRegisterDate();
    }
}
