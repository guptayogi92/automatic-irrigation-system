package com.automation.irrigationsystem.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Request model for adding new plot")
public class NewPlotRequest {

    @ApiModelProperty(notes = "Plot name", dataType = "java.lang.String", required = true)
    private String plotName;
    private String description;
    private String address;
    private String width;
    private String height;
    @ApiModelProperty(notes = "width height unit, valid unit are: FOOT, METER", required = true, value = "FOOT,METER")
    private String unit;
    @ApiModelProperty(notes = "Type of the schedule, valid values are REGULAR,CUSTOM", required = true, value = "REGULAR,CUSTOM")
    private String scheduleType;

    private String deviceId;
    @ApiModelProperty(notes = "Crop Name, valid values are RICE,WHEAT,MILLETS,PULSES,TEA,COFFEE,SUGARCANE,COTTON,JUTE", required = true, value = "RICE,WHEAT,MILLETS,PULSES,TEA,COFFEE,SUGARCANE,COTTON,JUTE")
    private String cropName;
    @ApiModelProperty(notes = "Soil Type, valid values are ALLUVIAL_SOIL,RED_SOIL,BLACK_SOIL,DESERT_SOIL,LATERITE_SOIL,SALINE_SOIL,PEATY_SOIL,FOREST_SOIL,SNOWFIELD_SOIL,SUB_MOUNTAIN_SOIL", required = true, value = "ALLUVIAL_SOIL,RED_SOIL,BLACK_SOIL,DESERT_SOIL,LATERITE_SOIL,SALINE_SOIL,PEATY_SOIL,FOREST_SOIL,SNOWFIELD_SOIL,SUB_MOUNTAIN_SOIL")
    private String soilType;

}
