package com.automation.irrigationsystem.response;

import lombok.Data;
import java.util.Date;

@Data
public class ScheduleResponse {

    private String scheduleName;

    private String description;

    private String irrigationDay;

    private String startTime;

    private String hoursToIrrigate;

    private Date createdDt;
}
