package com.automation.irrigationsystem.iface;

import com.automation.irrigationsystem.request.EditPlotRequest;
import com.automation.irrigationsystem.request.NewPlotRequest;
import com.automation.irrigationsystem.response.GetAllPlotResponse;
import com.automation.irrigationsystem.response.NewPlotResponse;

public interface IPlotService {

    NewPlotResponse addNewPlot(String customerId, NewPlotRequest newPlotRequest);

    GetAllPlotResponse getAllPlots(String customerId);

    void updatePlot(String customerId, String plotId, EditPlotRequest editPlotReq);

    void deletePlot(String customerId, String plotId);
}
