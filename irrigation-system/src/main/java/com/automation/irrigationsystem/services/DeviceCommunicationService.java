package com.automation.irrigationsystem.services;

import com.automation.irrigationsystem.exception.SensorNotAvailableException;
import com.automation.irrigationsystem.iface.IDeviceCommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class DeviceCommunicationService implements IDeviceCommunicationService {

    @Value("${irrigation.schedule.failedDeviceId:0}")
    private int failedDeviceId;

    @Override
    public void startCommunication(BigDecimal deviceId, int hoursToIrrigate, BigDecimal plotId) throws SensorNotAvailableException {
        log.info("-----Starting to irrigate the plot: {} with Device Id: {} ", plotId, deviceId);
        if(failedDeviceId == deviceId.intValue()) {
            throw new SensorNotAvailableException(deviceId.toString()
                    , plotId.toString(), String.valueOf(hoursToIrrigate), new Date()
                    ,"Currently Device is not available to irrigate the plot");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            throw new SensorNotAvailableException(deviceId.toString()
                    , plotId.toString(), String.valueOf(hoursToIrrigate), new Date()
                    ,"Something went wrong while sending the irrigation request");
        }
        log.info("-----Irrigation COMPLETED for the plot: {} with Device Id: {} ", plotId, deviceId);
    }
}
