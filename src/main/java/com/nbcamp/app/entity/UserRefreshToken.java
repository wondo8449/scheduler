package com.nbcamp.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table
public class UserRefreshToken {

    @Id
    private Long userNumber;

    // 회원 한명당 RefreshToken은 1개만 가지기 때문에 OnetoOne으로 해준다
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_number")
    private User user;

    private String refreshToken;

    public UserRefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}