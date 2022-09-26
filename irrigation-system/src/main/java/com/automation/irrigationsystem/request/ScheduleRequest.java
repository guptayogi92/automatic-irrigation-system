package com.automation.irrigationsystem.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Add new schedule request")
public class ScheduleRequest {

    private String scheduleName;

    private String description;
    @ApiModelProperty(notes = "Irrigation Day valid values are: DAILY,BIWEEKLY,WEEKLY", required = true, value = "DAILY,BIWEEKLY,WEEKLY")
    private String irrigationDay;

    private String hoursToIrrigate;

    @ApiModelProperty(notes = "Irrigation start time in HH24:MM format. In case of empty 08:00 will be configured as default", required = false, dataType = "java.lang.String", value = "value in the format of HH24:MM")
    private String startTime;

}
