package com.ddiring.BackEnd_Escrow.common.exception;

import org.springframework.http.HttpStatus;

public class ClientErrorException extends ApplicationException {
    public ClientErrorException() {
        super(ErrorCode.CLIENT_ERROR);
    }

    public ClientErrorException(String customMessage) {
        super(ErrorCode.CLIENT_ERROR, customMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}