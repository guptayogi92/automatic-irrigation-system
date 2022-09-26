package com.automation.irrigationsystem.common;

public class ResponseCodes {

    private ResponseCodes() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS = "00000000";
    public static final String EMPTY_INPUT = "00001001";
    public static final String INVALID_INPUT = "00001002";
    public static final String DATA_NOT_FOUND = "00001003";

    public static final String INTERNAL_SERVER_ERROR = "00001101";

    public static final String SUCCESS_MSG = "SUCCESS";
    public static final String CREATED_MSG = "CREATED";

    public static final String INTERNAL_SERVER_ERROR_MSG = "Internal server error. Kindly retry, if problem persist then contact system support";
}
