package com.ddiring.BackEnd_Escrow.common.exception;

public enum ErrorCode {
    INVALID_PARAMETER("InvalidParameter", "잘못된 파라미터입니다."),
    RESOURCE_NOT_FOUND("NotFound", "리소스를 찾을 수 없습니다."),
    CLIENT_ERROR("ClientError", "클라이언트 요청에 문제가 있습니다."),
    SERVER_ERROR("ServerError", "서버 오류입니다.");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String code() { return code; }
    public String defaultMessage() { return defaultMessage; }
}
