package com.nbcamp.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNumber;

    @Column(nullable = false)
    private String userNickname;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String userNickname, String username, String userPassword, UserRoleEnum role) {
        this.userNickname = userNickname;
        this.userName = username;
        this.userPassword = userPassword;
        this.role = role;
    }
}
