package com.automation.irrigationsystem.response;

import lombok.Data;

import java.util.List;

@Data
public class GetAllPlotResponse extends Response {

    private List<PlotResponse> plotList;
}
