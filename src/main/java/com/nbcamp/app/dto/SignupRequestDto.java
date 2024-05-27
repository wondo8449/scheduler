package com.nbcamp.app.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    // Controller단에서 @Valid로 검증하기 위한 정규식 조건 추가
    @Pattern(message = "아이디는 4자 이상, 10자 이하이고 알파벳 소문자, 숫자로 구성되어야 합니다.", regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$")
    private String userName;

    // Controller단에서 @Valid로 검증하기 위한 정규식 조건 추가
    @Pattern(message = "비밀번호는 8자 이상, 15자 이하이고 알파벳 대소문자, 숫자로 구성되어야 합니다.", regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,15}$")
    private String userPassword;

    private String userNickname;

    // 이걸 true로 입력해서 요청하면 관리자로 회원가입 가능
    private boolean admin = false;

    // 관리자 토큰을 입력하는 부분
    private String adminToken = "";
}
