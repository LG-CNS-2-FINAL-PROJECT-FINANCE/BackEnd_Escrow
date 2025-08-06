package com.ddiring.BackEnd_Escrow.advice;

import com.ddiring.BackEnd_Escrow.common.dto.ApiResponseDto;
import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonAdvice {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponseDto<String>> ApplicationException(ApplicationException ex) {
        log.warn("Application error: code={}, message={}", ex.getErrorCode(), ex.getErrorMessage());
        ApiResponseDto<String> body = ApiResponseDto.createError(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getErrorMessage(),
                null
        );
        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> UnexpectedException(Exception ex) {
        log.error("Unexpected server error", ex);
        ApiResponseDto<String> body = ApiResponseDto.createError(
                ErrorCode.SERVER_ERROR.status().value(),
                ErrorCode.SERVER_ERROR.code(),
                ErrorCode.SERVER_ERROR.defaultMessage(),
                null
        );
        return  ResponseEntity.status(500).body(body);
    }
}
