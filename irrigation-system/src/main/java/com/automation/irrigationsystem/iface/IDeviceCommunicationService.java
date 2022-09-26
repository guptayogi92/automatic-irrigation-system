package com.automation.irrigationsystem.iface;

import com.automation.irrigationsystem.exception.SensorNotAvailableException;

import java.math.BigDecimal;

public interface IDeviceCommunicationService {
    void startCommunication(BigDecimal deviceId, int hoursToIrrigate, BigDecimal plotId) throws SensorNotAvailableException;
}
