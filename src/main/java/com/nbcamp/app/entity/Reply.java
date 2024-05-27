package com.nbcamp.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Reply extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNumber;

    @Column(nullable = false)
    private String replyContent;

    // 댓글이 Many 유저가 One
    // FetchType.LAZY로 해야지 Entity의 정보를 바로 가져오기 않고
    // 필요할 때만 가져올 수 있게 된다 (지연로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number", nullable = false)
    private User user;

    // 댓글이 Many 일정이 One
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_number", nullable = false)
    private Board board;

    // ReplyRequestDto에서 사용하기 위해 @Builder로 설정해준다
    @Builder
    public Reply(String replyContent, Board board, User user) {
        this.replyContent = replyContent;
        this.board = board;
        this.user = user;
    }
}
