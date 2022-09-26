package com.automation.irrigationsystem.requestmockdata;

import com.automation.irrigationsystem.request.ScheduleRequest;

public class ScheduleRequestMockData {

    public static ScheduleRequest getSchData() {
        ScheduleRequest request = new ScheduleRequest();
        request.setScheduleName("WHEAT");
        request.setDescription("WHEAT_RED_SOIL_AUTUMN");
        request.setIrrigationDay("DAILY");
        request.setHoursToIrrigate("1");
        request.setStartTime("09:00");
        return request;
    }
}
