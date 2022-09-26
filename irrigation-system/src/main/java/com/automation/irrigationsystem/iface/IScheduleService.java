package com.automation.irrigationsystem.iface;

import com.automation.irrigationsystem.request.ScheduleRequest;
import com.automation.irrigationsystem.response.NewScheduleResponse;

public interface IScheduleService {

    NewScheduleResponse addNewCustomSchedule(String customerId, String plotId, ScheduleRequest newScheduleRequest);

    void updateCustomSchedule(String customerId, String plotId, String scheduleId, ScheduleRequest editScheduleRequest);

    void deleteCustomSchedule(String customerId, String plotId, String scheduleId);
}
