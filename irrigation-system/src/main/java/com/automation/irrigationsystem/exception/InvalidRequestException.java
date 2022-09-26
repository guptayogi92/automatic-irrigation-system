package com.automation.irrigationsystem.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {

    private final String responseCode;
    private final String responseMessage;

    public InvalidRequestException(String responseCode, String responseMessage) {
        super(responseMessage + " ResponseCode: "+ responseCode);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}