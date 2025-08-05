package com.ddiring.BackEnd_Escrow.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String overrideMessage;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
        this.overrideMessage = null;
    }

    public ApplicationException(ErrorCode errorCode, String overrideMessage) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
        this.overrideMessage = overrideMessage;
    }

    public  abstract HttpStatus getStatus();

    public String getErrorCode() { return errorCode.code(); }
    public String getErrorMessage() { return overrideMessage != null ? overrideMessage : errorCode.defaultMessage(); }
}