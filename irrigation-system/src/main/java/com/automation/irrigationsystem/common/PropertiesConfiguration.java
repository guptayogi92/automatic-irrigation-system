package com.automation.irrigationsystem.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfiguration {

    @Value("${irrigation.schedule.failedDeviceId:0}")
    private int failedDeviceId;

    @Value("${irrigation.schedule.defaultStartTime}")
    String defaultStartTime;
}
