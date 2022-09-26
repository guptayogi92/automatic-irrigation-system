package com.automation.irrigationsystem.controller;

import com.automation.irrigationsystem.iface.IPlotService;
import com.automation.irrigationsystem.request.EditPlotRequest;
import com.automation.irrigationsystem.request.NewPlotRequest;
import com.automation.irrigationsystem.response.GetAllPlotResponse;
import com.automation.irrigationsystem.response.NewPlotResponse;
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
@Api(tags = {"Plot related management"})
@RequestMapping("/irrigation/plotmanagement/customer/{customer_id}")
public class PlotController {

    @Autowired
    private IPlotService iPlotService;

    @PostMapping(value = "", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to Configure a new plot")
    public ResponseEntity<NewPlotResponse> addNewPlot(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId,
                                                      @RequestBody final NewPlotRequest newPlotRequest) {
        log.info(">>>>>>>>>> Adding new plot for the customer id: {}", customerId);
        return new ResponseEntity<>(iPlotService.addNewPlot(customerId, newPlotRequest), HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to retrieve available plots for a customer")
    public ResponseEntity<GetAllPlotResponse> getAllPlot(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId) {
        log.info(">>>>>>>>>> Retrieving all the plot for the customer id: {}", customerId);
        return ResponseEntity.ok(iPlotService.getAllPlots(customerId));
    }

    @PutMapping(value= "plot/{plot_id}", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to update the plot for a customer")
    public ResponseEntity<Response> updatePlot(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId
            , @ApiParam(value = "Plot Id to update Plot") @PathVariable("plot_id") final String plotId
            , @RequestBody final EditPlotRequest editPlotReq) {
        log.info(">>>>>>>>>> Updating plot for the customer id: {}, plot id: {}", customerId, plotId);
        iPlotService.updatePlot(customerId, plotId, editPlotReq);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value= "plot/{plot_id}", produces = {"application/json", "application/xml"})
    @ApiOperation(value = "This API is used to Delete the plot for a customer")
    public ResponseEntity<Response> deletePlot(@ApiParam(value = "Customer Id of the plot") @PathVariable("customer_id") final String customerId
            , @ApiParam(value = "Plot Id to delete Plot") @PathVariable("plot_id") final String plotId) {
        log.info(">>>>>>>>>> Deleting plot for the customer id: {}, plot id: {}", customerId, plotId);
        iPlotService.deletePlot(customerId, plotId);
        return ResponseEntity.noContent().build();
    }
}