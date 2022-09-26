package com.automation.irrigationsystem.common;

public enum SchedulingState {
    CONFIGURED,     //When irrigation has not started.
    IN_PROGRESS,    //Request send to device but waiting for error or success confirmation.
    RETRY,          //When device is not responding
    COMPLETED,      //Irrigation request sent to device successfully.
    FAILED,         //Irrigation failed after configured retries.
}
