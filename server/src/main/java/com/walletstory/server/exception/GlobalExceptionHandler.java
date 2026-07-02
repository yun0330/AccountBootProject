package com.walletstory.server.exception;

import com.walletstory.server.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO> handleCustomException(CustomException e) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .error(e.getMessage())
                .build();
        return ResponseEntity.status(e.getStatus()).body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception e) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .error("서버 오류가 발생했습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }



}
