package com.nbcamp.app.controller;

import com.nbcamp.app.dto.LoginRequestDto;
import com.nbcamp.app.dto.SignupRequestDto;
import com.nbcamp.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequestDto requestDto) {
        try {
            return userService.signup(requestDto);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            return userService.login(loginRequestDto);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }


}
