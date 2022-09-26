package com.automation.irrigationsystem.mockdata;

import com.automation.irrigationsystem.entity.CustomSchedule;

import java.math.BigDecimal;
import java.util.Date;

public class CustomScheduleMockData {

    public static CustomSchedule getCustomSchedule() {
        CustomSchedule cs = new CustomSchedule();
        cs.setId(BigDecimal.ONE);
        cs.setCustomerId(BigDecimal.ONE);
        cs.setScheduleName("CUSTOM SCHEDULE");
        cs.setDescription("CUSTOM SCHEDULE DESCRIPTION");
        cs.setIrrigationDay("DAILY");
        cs.setStartTime("02:00");
        cs.setHoursToIrrigate(BigDecimal.ONE);
        cs.setActiveFlg(BigDecimal.ONE);
        cs.setCreatedDt(new Date());
        return cs;
    }

}
