package com.automation.irrigationsystem.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class SensorNotAvailableException extends Exception {

    private final String sensorDeviceId;

    private final String plotId;

    private final String hoursToIrrigate;

    private final Date timeStamp;

    private final String message;

    public SensorNotAvailableException(String sensorDeviceId, String plotId, String hoursToIrrigate, Date timeStamp, String message) {
        super(message);
        this.sensorDeviceId = sensorDeviceId;
        this.plotId = plotId;
        this.hoursToIrrigate = hoursToIrrigate;
        this.timeStamp = timeStamp;
        this.message = message;
    }
}