package com.automation.irrigationsystem.mockdata;

import com.automation.irrigationsystem.entity.Schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleMockData {

    public static Schedule getScheduleMockData() {
        Schedule sch = new Schedule();
        sch.setId(BigDecimal.ONE);
        sch.setScheduleName("WHEAT_RED_SOIL_AUTUMN");
        sch.setDescription("WHEAT_RED_SOIL_AUTUMN");
        sch.setCropName("WHEAT");
        sch.setSoilType("RED SOIL");
        sch.setWeatherType("AUTUMN");
        sch.setIrrigationDay("DAILY");
        sch.setHoursToIrrigate(BigDecimal.ONE);
        sch.setActiveFlg(BigDecimal.ONE);
        sch.setCreatedDt(new Date());
        return sch;
    }

    public static List<Schedule> getSchduleList(){
        List<Schedule> schList = new ArrayList<>();
        schList.add(getScheduleMockData());
        return schList;
    }
}
