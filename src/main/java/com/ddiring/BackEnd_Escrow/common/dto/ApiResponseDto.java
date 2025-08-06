package com.ddiring.BackEnd_Escrow.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponseDto<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public ApiResponseDto(int status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseDto<T> createOk(T data) {
        return new ApiResponseDto<>(200, "OK", "요청을 성공하였습니다.", data);
    }

    public static ApiResponseDto<String> defaultOk() {
        return ApiResponseDto.createOk(null);
    }

    public static <T> ApiResponseDto<T> createError(int status, String code, String message, T data) {
        return new ApiResponseDto<>(status, code, message, data);
    }
}
