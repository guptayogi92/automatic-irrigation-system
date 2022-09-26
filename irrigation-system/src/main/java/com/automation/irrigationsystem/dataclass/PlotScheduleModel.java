package com.automation.irrigationsystem.dataclass;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlotScheduleModel {

    private BigDecimal plotId;

    private BigDecimal customerId;

    private String plotName;

    private String plotDescription;

    private String address;

    private BigDecimal width;

    private BigDecimal height;

    private String unit;

    private String scheduleType;

    private BigDecimal customSchedId;

    private BigDecimal deviceId;

    private String cropName;

    private String soilType;

    private String scheduleName;

    private String scheduleDescription;

    private String irrigationDay;

    private BigDecimal hoursToIrrigate;

    private String startTime;
}
