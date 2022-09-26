package com.automation.irrigationsystem.controller;

import com.automation.irrigationsystem.iface.IScheduleService;
import com.automation.irrigationsystem.request.ScheduleRequest;
import com.automation.irrigationsystem.response.NewScheduleResponse;
import com.automation.irrigationsystem.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = {"Irrigation schedule related management"})
@RequestMapping("/irrigation/schedule/customer/{customer_id}")
public class ScheduleController {

    @Autowired
    private IScheduleService iScheduleService;

    @PostMapping(value = "/plot/{plot_id}", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to Configure a new schedule for the plot")
    public ResponseEntity<NewScheduleResponse> addNewCustomSchedule(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId
            , @ApiParam(value = "Plot Id to configure schedule") @PathVariable("plot_id") final String plotId
            , @RequestBody final ScheduleRequest newScheduleRequest) {
        log.info(">>>>>>>>>> Adding new schedule for the customer id: {}, plot id: {}", customerId, plotId);
        return new ResponseEntity<>(iScheduleService.addNewCustomSchedule(customerId, plotId, newScheduleRequest), HttpStatus.CREATED);
    }

    @PutMapping(value = "/plot/{plot_id}/schedule/{schedule_id}", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to Edit the schedule for a customer")
    public ResponseEntity<Response> updateCustomSchedule(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId
            , @ApiParam(value = "Plot Id to edit schedule") @PathVariable("plot_id") final String plotId
            , @ApiParam(value = "Schedule Id to edit") @PathVariable("schedule_id") final String scheduleId
            , @RequestBody final ScheduleRequest editScheduleRequest) {
        log.info(">>>>>>>>>> Editing schedule for the customer id: {}, plot id: {}, schedule id: {}", customerId, plotId, scheduleId);
        iScheduleService.updateCustomSchedule(customerId, plotId, scheduleId, editScheduleRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/plot/{plot_id}/schedule/{schedule_id}", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to Delete the schedule for a customer")
    public ResponseEntity<Response> deleteCustomSchedule(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId
            , @ApiParam(value = "Plot Id to delete schedule") @PathVariable("plot_id") final String plotId
            , @ApiParam(value = "Schedule Id to delete") @PathVariable("schedule_id") final String scheduleId) {
        log.info(">>>>>>>>>> Deleting schedule for the customer id: {}, plot id: {}, schedule id: {}", customerId, plotId, scheduleId);
        iScheduleService.deleteCustomSchedule(customerId, plotId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}