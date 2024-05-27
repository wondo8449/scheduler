package com.nbcamp.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
    private String boardPassword;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number", nullable = false)
    private User user;

    @Builder
    public Board(String boardTitle, String boardContent, String boardPassword, User user) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardPassword = boardPassword;
        this.user = user;
    }
}
