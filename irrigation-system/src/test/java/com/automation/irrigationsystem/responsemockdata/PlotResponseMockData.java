package com.automation.irrigationsystem.responsemockdata;

import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.response.PlotResponse;

public class PlotResponseMockData {

    public static PlotResponse getPlotResponse(Plot plot){
        PlotResponse resp = new PlotResponse();
        resp.setPlotId(plot.getId().toString());
        resp.setPlotName(plot.getPlotName());
        resp.setDescription(plot.getDescription());
        resp.setCustomerId(plot.getCustomerId().toString());
        resp.setAddress(plot.getAddress());
        resp.setWidth(plot.getWidth().toString());
        resp.setHeight(plot.getHeight().toString());
        resp.setUnit(plot.getUnit());
        resp.setScheduleType(plot.getScheduleType());
        resp.setDeviceId(plot.getDeviceId().toString());
        resp.setCropName(plot.getCropName());
        resp.setSoilType(plot.getSoilType());
        return resp;
    }
}