package com.automation.irrigationsystem.mockdata;

import com.automation.irrigationsystem.requestmockdata.NewPlotRequestMockData;
import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.request.NewPlotRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlotMockData {

    public static Plot getPlot(String id, String customerId, NewPlotRequest newPlotRequest) {
        Plot plot = new Plot();
        plot.setId(new BigDecimal(id));
        plot.setPlotName(newPlotRequest.getPlotName().toUpperCase()+ "-" +id);
        plot.setDescription(newPlotRequest.getDescription()+ "-" +id);
        plot.setCustomerId(new BigDecimal(customerId.trim()));
        plot.setAddress(newPlotRequest.getAddress()+ "-" +id);
        plot.setWidth(new BigDecimal(newPlotRequest.getWidth().trim()));
        plot.setHeight(new BigDecimal(newPlotRequest.getHeight().trim()));
        plot.setUnit(newPlotRequest.getUnit().toUpperCase());
        plot.setScheduleType(newPlotRequest.getScheduleType().toUpperCase());
        plot.setDeviceId(new BigDecimal(newPlotRequest.getDeviceId().trim()));
        plot.setSoilType(newPlotRequest.getSoilType().toUpperCase());
        plot.setCropName(newPlotRequest.getCropName().toUpperCase());
        plot.setActiveFlg(BigDecimal.ONE);
        plot.setCreatedDt(new Date());
        return plot;
    }

    public static List<Plot> regPlotListWithoutSched() {
        List<Plot> plotList = new ArrayList<>();
        Plot plot = getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched());
        plot.setCropName("DUMMY CROP");
        plotList.add(plot);
        return plotList;
    }

    public static List<Plot> regPlotListWithSched() {
        List<Plot> plotList = new ArrayList<>();
        plotList.add(getPlot("1", "2", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
        return plotList;
    }

    public static List<Plot> customPlotListWithoutSched() {
        List<Plot> plotList = new ArrayList<>();
        plotList.add(getPlot("1", "3", NewPlotRequestMockData.getNewPlotRequestCustomSched()));
        return plotList;
    }

    public static List<Plot> customPlotListWithSched() {
        List<Plot> plotList = new ArrayList<>();
        Plot plot = getPlot("1", "4", NewPlotRequestMockData.getNewPlotRequestCustomSched());
        plot.setCustomSchedId(BigDecimal.ONE);
        plot.setCustomSchedule(CustomScheduleMockData.getCustomSchedule());
        plotList.add(plot);
        return plotList;
    }

}