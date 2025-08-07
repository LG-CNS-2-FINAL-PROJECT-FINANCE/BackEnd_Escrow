package com.ddiring.BackEnd_Escrow.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_PARAMETER("INVALID_PARAMETER", "잘못된 파라미터입니다.", HttpStatus.BAD_REQUEST),
    SERVER_ERROR("SERVER_ERROR", "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_ACCOUNT("DUPLICATE_ACCOUNT", "이미 존재하는 계좌입니다.", HttpStatus.CONFLICT),
    ESCROW_NOT_FOUND("ESCROW_NOT_FOUND", "해당 프로젝트의 에스크로 계좌가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ESCROW_HISTORY_NOT_FOUND("ESCROW_HISTORY_NOT_FOUND", "해당 에스크로 계좌의 거래 내역이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ESCROW_BALANCE_NOT_FOUND("ESCROW_BALANCE_NOT_FOUND", "해당 에스크로 계좌의 잔액 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TRANS_TYPE("INVALID_TRANS_TYPE", "transType은 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
    DEPOSIT_AMOUNT_INVALID("DEPOSIT_AMOUNT_INVALID", "입금 금액은 0원보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    WITHDRAW_AMOUNT_INVALID("WITHDRAW_AMOUNT_INVALID", "출금 금액은 0원보다 커야 합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String code() { return code; }
    public String defaultMessage() { return defaultMessage; }
    public HttpStatus status() { return httpStatus; }
}
