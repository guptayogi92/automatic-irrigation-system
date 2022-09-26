package com.automation.irrigationsystem.exception;

import com.automation.irrigationsystem.common.ResponseCodes;
import com.automation.irrigationsystem.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class IrrigationExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Response> dataNotFoundExceptionHandler(DataNotFoundException ex, WebRequest request) {
        log.debug("Data Not Found Exception: _RESPONSE_CODE: {}, _RESPONSE_MESSAGE: {} ", ex.getResponseCode(), ex.getResponseMessage());
        Response rs = new Response();
        rs.setResponseCode(ex.getResponseCode());
        rs.setResponseMessage(ex.getResponseMessage());
        rs.setResponseTimeStamp(new Date());
        return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Response> invalidRequestExceptionHandler(InvalidRequestException ex, WebRequest request) {
        log.debug("Invalid Request Exception: _RESPONSE_CODE: {}, _RESPONSE_MESSAGE: {} ", ex.getResponseCode(), ex.getResponseMessage());
        Response rs = new Response();
        rs.setResponseCode(ex.getResponseCode());
        rs.setResponseMessage(ex.getResponseMessage());
        rs.setResponseTimeStamp(new Date());
        return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> invalidRequestExceptionHandler(Exception ex, WebRequest request) {
        log.debug("****** Unknown exception triggered in system, Kindly look into this {}", ex.getMessage(), ex);
        Response rs = new Response();
        rs.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR);
        rs.setResponseMessage(ResponseCodes.INTERNAL_SERVER_ERROR_MSG);
        rs.setResponseTimeStamp(new Date());
        return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
