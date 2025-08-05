package com.ddiring.BackEnd_Escrow.common.exception;

import org.springframework.http.HttpStatus;

public class BadParameterException extends ApplicationException {
    public BadParameterException() {
        super(ErrorCode.INVALID_PARAMETER);
    }

    public BadParameterException(String customMessage) {
        super(ErrorCode.INVALID_PARAMETER, customMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}