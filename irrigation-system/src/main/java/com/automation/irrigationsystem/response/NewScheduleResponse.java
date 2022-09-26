package com.automation.irrigationsystem.response;

import lombok.Data;

import java.util.Date;

@Data
public class NewScheduleResponse extends Response {

    private String scheduleId;

    private String scheduleName;

    private String description;

    private String irrigationDay;

    private String hoursToIrrigate;

    private String plotId;

    private String customerId;

    private Date createdDt;
}
