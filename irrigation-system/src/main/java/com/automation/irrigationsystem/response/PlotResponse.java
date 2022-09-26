package com.automation.irrigationsystem.response;

import lombok.Data;

@Data
public class PlotResponse {

    private String plotId;

    private String customerId;

    private String plotName;

    private String description;

    private String address;

    private String width;

    private String height;

    private String unit;

    private String scheduleType;

    private String customSchedId;

    private String deviceId;

    private String cropName;

    private String soilType;

    private ScheduleResponse schedule;
}