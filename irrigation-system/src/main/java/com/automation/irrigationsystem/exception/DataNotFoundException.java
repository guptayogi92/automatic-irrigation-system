package com.automation.irrigationsystem.exception;

import lombok.Data;

@Data
public class DataNotFoundException extends RuntimeException {

    private final String responseCode;
    private final String responseMessage;

    public DataNotFoundException(String responseCode, String responseMessage) {
        super(responseMessage + " ResponseCode: "+ responseCode);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}