package com.automation.irrigationsystem.requestmockdata;

import com.automation.irrigationsystem.request.EditPlotRequest;
import com.automation.irrigationsystem.request.NewPlotRequest;

public class EditPlotRequestMockData {

    public static EditPlotRequest getEditPlotReqData(NewPlotRequest newPlotRequest) {
        EditPlotRequest req = new EditPlotRequest();

        req.setPlotName(newPlotRequest.getPlotName());
        req.setDescription(newPlotRequest.getDescription());
        req.setAddress(newPlotRequest.getAddress());
        req.setWidth(newPlotRequest.getWidth());
        req.setHeight(newPlotRequest.getHeight());
        req.setUnit(newPlotRequest.getUnit());
        req.setScheduleType(newPlotRequest.getScheduleType());
        req.setDeviceId(newPlotRequest.getDeviceId());
        req.setCropName(newPlotRequest.getCropName());
        req.setSoilType(newPlotRequest.getSoilType());
        req.setScheduleId("");
        return req;
    }
}