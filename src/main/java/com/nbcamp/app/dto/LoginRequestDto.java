package com.nbcamp.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String userName;
    private String userPassword;
}
