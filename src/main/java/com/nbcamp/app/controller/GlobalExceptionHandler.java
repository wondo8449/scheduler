package com.nbcamp.app.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Valid 예외 처리
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage() , HttpStatus.BAD_REQUEST);
    }

    // PathVariable로 값이 들어오지 않을 경우 예외처리
    @ExceptionHandler({NoResourceFoundException.class})
    protected ResponseEntity handleNoResourceFoundException(NoResourceFoundException e) {
        return new ResponseEntity<>(e.getBody().getDetail(), HttpStatus.BAD_REQUEST);
    }
}
