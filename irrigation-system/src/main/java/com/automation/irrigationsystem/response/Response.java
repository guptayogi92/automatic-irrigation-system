package com.automation.irrigationsystem.response;

import lombok.Data;

import java.util.Date;
@Data
public class Response {

    private String responseCode;
    private String responseMessage;
    private Date responseTimeStamp;
}
