package com.automation.irrigationsystem.requestmockdata;

import com.automation.irrigationsystem.request.NewPlotRequest;

public class NewPlotRequestMockData {

    public static NewPlotRequest getNewPlotRequestRegularSched() {
        NewPlotRequest req = new NewPlotRequest();
        req.setPlotName("DUMMY PlOT");
        req.setDescription("DUMMY DESCRIPTION");
        req.setAddress("DUMMY ADDRESS");
        req.setWidth("10");
        req.setHeight("20");
        req.setUnit("FOOT");
        req.setScheduleType("REGULAR");
        req.setDeviceId("1");
        req.setCropName("WHEAT");
        req.setSoilType("RED_SOIL");
        return req;
    }

    public static NewPlotRequest getNewPlotRequestCustomSched() {
        NewPlotRequest req = new NewPlotRequest();
        req.setPlotName("DUMMY PlOT");
        req.setDescription("DUMMY DESCRIPTION");
        req.setAddress("DUMMY ADDRESS");
        req.setWidth("10");
        req.setHeight("20");
        req.setUnit("FOOT");
        req.setScheduleType("CUSTOM");
        req.setDeviceId("1");
        req.setCropName("WHEAT");
        req.setSoilType("RED_SOIL");
        return req;
    }
}