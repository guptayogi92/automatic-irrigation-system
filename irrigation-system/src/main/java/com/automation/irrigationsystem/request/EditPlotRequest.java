package com.automation.irrigationsystem.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Edit plot Request")
public class EditPlotRequest extends NewPlotRequest {

    @ApiModelProperty(notes = "new schedule id to link with plot, null if don't want to edit", required = false, dataType = "java.lang.Integer")
    private String scheduleId;
}
