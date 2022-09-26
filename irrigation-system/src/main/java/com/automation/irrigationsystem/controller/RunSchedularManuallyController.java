package com.automation.irrigationsystem.controller;

import com.automation.irrigationsystem.common.ResponseCodes;
import com.automation.irrigationsystem.config.ConfigureIrrigationScheduler;
import com.automation.irrigationsystem.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@Api(tags = {"Run Configuration scheduler manually"})
@RequestMapping("/irrigation/trigger_daily_sched_manually")
public class RunSchedularManuallyController {

    @Autowired
    ConfigureIrrigationScheduler configureIrrigationScheduler;

    @ApiOperation(value = "This API is used to trigger the scheduler job manually.")
    @PostMapping(value = "", produces = {"application/json", "application/xml"})
    public ResponseEntity<Response> runSchedularManually() {
        log.info(">>>>>>>> Triggering the daily scheduler manually");
        configureIrrigationScheduler.oneTimeSchedulerADay();
        Response response = new Response();
        response.setResponseCode(ResponseCodes.SUCCESS);
        response.setResponseMessage(ResponseCodes.CREATED_MSG);
        response.setResponseTimeStamp(new Date());
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
