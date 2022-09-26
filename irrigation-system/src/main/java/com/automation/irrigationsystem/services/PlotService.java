package com.automation.irrigationsystem.services;

import com.automation.irrigationsystem.common.*;
import com.automation.irrigationsystem.entity.CustomSchedule;
import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.entity.Schedule;
import com.automation.irrigationsystem.exception.DataNotFoundException;
import com.automation.irrigationsystem.iface.IPlotService;
import com.automation.irrigationsystem.repository.CustomScheduleRepository;
import com.automation.irrigationsystem.repository.PlotRepository;
import com.automation.irrigationsystem.repository.ScheduleRepository;
import com.automation.irrigationsystem.request.EditPlotRequest;
import com.automation.irrigationsystem.request.NewPlotRequest;
import com.automation.irrigationsystem.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PlotService implements IPlotService {

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomScheduleRepository customScheduleRepository;

    @Value("${irrigation.schedule.defaultStartTime}")
    String defaultStartTime;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public NewPlotResponse addNewPlot(String customerId, NewPlotRequest newPlotRequest) {
        validateAddNewPlotRequest(customerId, newPlotRequest);
        return convertPlotToResponseObj(buildPlotObj(customerId, newPlotRequest));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
    public GetAllPlotResponse getAllPlots(String customerId) {
        log.info("Service: Getting the plots");
        validateGetPlotReq(customerId);
        List<Plot> plotList = plotRepository.findByCustomerIdAndActiveFlg(new BigDecimal(customerId.trim())
                , BigDecimal.ONE);
        if(CollectionUtils.isEmpty(plotList)) {
           throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Customer ID doesn't Exist");
        }
        GetAllPlotResponse resp = new GetAllPlotResponse();
        List<PlotResponse> plotResponseList = new ArrayList<>();
        plotList.forEach(e -> plotResponseList.add(buildPlotResponse(e)));
        resp.setPlotList(plotResponseList);
        resp.setResponseCode(ResponseCodes.SUCCESS);
        resp.setResponseMessage(ResponseCodes.SUCCESS_MSG);
        resp.setResponseTimeStamp(new Date());
        log.info("<<<<<<<< All the plots retrieved successfully for the customer: {}, Plot count is: {}"
                , customerId, plotResponseList.size());
        return resp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePlot(String customerId, String plotId, EditPlotRequest editPlotReq) {
        validateUpdatePlotReq(customerId, plotId, editPlotReq);
        Plot plot = plotRepository.findByCustomerIdAndIdAndActiveFlg(new BigDecimal(customerId.trim())
                , new BigDecimal(plotId.trim()), BigDecimal.ONE);
        if(null == plot) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the customer_id and plot_id combination.");
        }
        buildAndUpdatePlot(editPlotReq, plot);
        log.info("<<<<<<<< Plot has been updated successfully for the customer id: {}, plot id: {}", customerId, plotId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePlot(String customerId, String plotId) {
        validateDeletePlotReq(customerId, plotId);
        Plot plot = plotRepository.findByCustomerIdAndIdAndActiveFlg(new BigDecimal(customerId.trim())
                , new BigDecimal(plotId.trim()), BigDecimal.ONE);
        if(null == plot) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the customer_id and plot_id combination.");
        }
        plot.setActiveFlg(BigDecimal.ZERO);
        plot.setModifiedDt(new Date());
        plotRepository.save(plot);
        log.info("<<<<<<<< Plot has been deleted successfully for the customer id: {}, plot id: {}", customerId, plotId);

    }

    private void validateDeletePlotReq(String customerId, String plotId) {
        validateGetPlotReq(customerId);

        CommonMethods.throwExceptionIfEmpty(plotId,
                "Plot Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(plotId,
                "Please input valid plot id.");

    }

    private void buildAndUpdatePlot(EditPlotRequest editPlotReq, Plot plot) {
        plot.setPlotName(CommonMethods.getDefaultValueIfNull(plot.getPlotName(), editPlotReq.getPlotName()));
        plot.setDescription(CommonMethods.getDefaultValueIfNull(plot.getDescription(), editPlotReq.getDescription()));
        plot.setAddress(CommonMethods.getDefaultValueIfNull(plot.getAddress(), editPlotReq.getAddress()));
        plot.setWidth(CommonMethods.getDefaultValueBDIfNull(plot.getWidth(), editPlotReq.getWidth()));
        plot.setHeight(CommonMethods.getDefaultValueBDIfNull(plot.getHeight(), editPlotReq.getHeight()));
        plot.setUnit(CommonMethods.getDefaultValueIfNull(plot.getUnit(), editPlotReq.getUnit()));
        plot.setScheduleType(CommonMethods.getDefaultValueIfNull(plot.getScheduleType(), editPlotReq.getScheduleType()));
        updateScheduleId(plot, editPlotReq.getScheduleId());
        plot.setDeviceId(CommonMethods.getDefaultValueBDIfNull(plot.getDeviceId(), editPlotReq.getDeviceId()));
        plot.setCropName(CommonMethods.getDefaultValueIfNull(plot.getCropName(), editPlotReq.getCropName()));
        plot.setSoilType(CommonMethods.getDefaultValueIfNull(plot.getSoilType(), editPlotReq.getSoilType()));
        plot.setModifiedDt(new Date());
        plotRepository.save(plot);
    }



    private void updateScheduleId(Plot plot, String newScheduleId) {
        if(plot.getScheduleType().equals(ScheduleTypes.CUSTOM.name())) {
            if(!CommonMethods.isBlank(newScheduleId)) {
                Optional<CustomSchedule> customScheduleOptional = customScheduleRepository
                        .findById(new BigDecimal(newScheduleId.trim()));
                if(!customScheduleOptional.isPresent()) {
                    throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Schedule Id doesn't Exist in the DB");
                }
                plot.setCustomSchedId(new BigDecimal(newScheduleId.trim()));
            }
        }else {
            plot.setCustomSchedId(null);
        }
    }



    private void validateUpdatePlotReq(String customerId, String plotId, EditPlotRequest editPlotReq) {
        CommonMethods.throwExceptionIfEmpty(plotId,
                "Plot Id must not be empty");

        CommonMethods.throwExceptionIfNotNumber(plotId,
                "Please input valid plot id.");

        validateEditNewCommonPlotObj(customerId, editPlotReq);
    }

    private PlotResponse buildPlotResponse(Plot plot) {
        PlotResponse resp = new PlotResponse();
        resp.setPlotId(plot.getId().toString());
        resp.setPlotName(plot.getPlotName());
        resp.setDescription(plot.getDescription());
        resp.setCustomerId(plot.getCustomerId().toString());
        resp.setAddress(plot.getAddress());
        resp.setWidth(plot.getWidth().toString());
        resp.setHeight(plot.getHeight().toString());
        resp.setUnit(plot.getUnit());
        resp.setScheduleType(plot.getScheduleType());
        resp.setDeviceId(plot.getDeviceId().toString());
        resp.setCropName(plot.getCropName());
        resp.setSoilType(plot.getSoilType());

        ScheduleResponse schedResp = new ScheduleResponse();

        if(plot.getScheduleType().equals(ScheduleTypes.CUSTOM.name())) {
            CustomSchedule customSched = plot.getCustomSchedule();
            if(null == customSched) {
                schedResp.setDescription("Kindly configure the schedule for your plot.");
            }else {
                schedResp.setScheduleName(customSched.getScheduleName());
                schedResp.setDescription(customSched.getDescription());
                schedResp.setIrrigationDay(customSched.getIrrigationDay());
                schedResp.setStartTime(customSched.getStartTime());
                schedResp.setHoursToIrrigate(customSched.getHoursToIrrigate().toString());
                schedResp.setCreatedDt(customSched.getCreatedDt());
            }
        }else {
            Schedule schedule = getScheduleForCrop(plot.getCropName(), plot.getSoilType());
            if(null == schedule) {
                schedResp.setDescription("Kindly contact with customer service to configure REGULAR type schedules.");
            }else {
                schedResp.setScheduleName(schedule.getScheduleName());
                schedResp.setDescription(schedule.getDescription());
                schedResp.setIrrigationDay(schedule.getIrrigationDay());
                schedResp.setStartTime(defaultStartTime);
                schedResp.setHoursToIrrigate(schedule.getHoursToIrrigate().toString());
                schedResp.setCreatedDt(schedule.getCreatedDt());
            }
        }
        resp.setSchedule(schedResp);
        return resp;
    }

    private Schedule getScheduleForCrop(String cropName, String soilType) {
        String weatherName = CommonMethods.getWeatherNameForDate(Calendar.getInstance());
        List<Schedule> scheduleList = scheduleRepository.findByCropNameAndSoilTypeAndWeatherTypeAndActiveFlg(
                cropName, soilType, weatherName, BigDecimal.ONE);
        if(!CollectionUtils.isEmpty(scheduleList)) {
            return scheduleList.get(0);
        }else {
            return null;
        }
    }

    private void validateGetPlotReq(String customerId) {
        CommonMethods.throwExceptionIfEmpty(customerId,
                "Customer Id must not be empty");

        CommonMethods.throwExceptionIfNotNumber(customerId,
                "Please input valid customer id.");
    }

    private NewPlotResponse convertPlotToResponseObj(Plot plot) {
        NewPlotResponse resp = new NewPlotResponse();
        resp.setPlotId(plot.getId().toString());
        resp.setPlotName(plot.getPlotName());
        resp.setDescription(plot.getDescription());
        resp.setCustomerId(plot.getCustomerId().toString());
        resp.setAddress(plot.getAddress());
        resp.setWidth(plot.getWidth().toString());
        resp.setHeight(plot.getHeight().toString());
        resp.setUnit(plot.getUnit());
        resp.setScheduleType(plot.getScheduleType());
        resp.setDeviceId(plot.getDeviceId().toString());
        resp.setCropName(plot.getCropName());
        resp.setSoilType(plot.getSoilType());
        resp.setResponseCode(ResponseCodes.SUCCESS);
        resp.setResponseMessage(ResponseCodes.CREATED_MSG);
        resp.setResponseTimeStamp(new Date());
        log.info("<<<<<<<< Plot has been added successfully, generated plot id is: {}", plot.getId());
        return resp;

    }

    private Plot buildPlotObj(String customerId, NewPlotRequest newPlotRequest) {
        Plot plot = new Plot();
        plot.setPlotName(newPlotRequest.getPlotName().toUpperCase());
        plot.setDescription(newPlotRequest.getDescription());
        plot.setCustomerId(new BigDecimal(customerId.trim()));
        plot.setAddress(newPlotRequest.getAddress());
        plot.setWidth(new BigDecimal(newPlotRequest.getWidth().trim()));
        plot.setHeight(new BigDecimal(newPlotRequest.getHeight().trim()));
        plot.setUnit(newPlotRequest.getUnit().toUpperCase());
        plot.setScheduleType(newPlotRequest.getScheduleType().toUpperCase());
        plot.setDeviceId(new BigDecimal(newPlotRequest.getDeviceId().trim()));
        plot.setSoilType(newPlotRequest.getSoilType().toUpperCase());
        plot.setCropName(newPlotRequest.getCropName().toUpperCase());
        plot.setActiveFlg(BigDecimal.ONE);
        plot.setCreatedDt(new Date());
        plot = plotRepository.save(plot);
        return plot;
    }

    private void validateAddNewPlotRequest(String customerId, NewPlotRequest newPlotRequest) {

        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getPlotName(),
                "Plot Name must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getAddress(),
                "Plot Address must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getWidth(),
                "Plot Width must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getHeight(),
                "Plot Height must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getUnit(),
                "Plot Unit must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getScheduleType(),
                "Plot Schedule Type must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getDeviceId(),
                "Plot Device Id must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getCropName(),
                "Crop Name must not be empty");
        CommonMethods.throwExceptionIfEmpty(newPlotRequest.getSoilType(),
                "Soil Type must not be empty");

        validateEditNewCommonPlotObj(customerId, newPlotRequest);
    }

    private void validateEditNewCommonPlotObj(String customerId, NewPlotRequest newPlotRequest) {
        CommonMethods.throwExceptionIfEmpty(customerId,
                "Customer Id must not be empty");

        CommonMethods.throwExceptionIfNotNumber(customerId,
                "Please input valid customer id.");
        CommonMethods.throwExceptionIfNotNumber(newPlotRequest.getWidth(),
                "Please input valid plot width.");
        CommonMethods.throwExceptionIfNotNumber(newPlotRequest.getHeight(),
                "Please input valid plot height.");
        CommonMethods.throwExceptionIfNotNumber(newPlotRequest.getDeviceId(),
                "Please input valid plot device id.");

        CommonMethods.throwExceptionIfLengthInvalid(newPlotRequest.getPlotName(), 50,
                "Plot Name should not be more than 50 character long");
        CommonMethods.throwExceptionIfLengthInvalid(newPlotRequest.getDescription(), 200,
                "Plot Description should not be more than 200 character long");
        CommonMethods.throwExceptionIfLengthInvalid(newPlotRequest.getAddress(), 200,
                "Plot Address should not be more than 200 character long");
        CommonMethods.throwExceptionIfLengthInvalid(newPlotRequest.getCropName(), 20,
                "Crop Name should not be more than 20 character long");
        CommonMethods.throwExceptionIfLengthInvalid(newPlotRequest.getSoilType(), 20,
                "Soil Type should not be more than 20 character long");


        CommonMethods.validateInputWithEnum(PlotUnit.class, newPlotRequest.getUnit()
                , "Please provide valid input for Plot Unit");
        CommonMethods.validateInputWithEnum(ScheduleTypes.class, newPlotRequest.getScheduleType()
                , "Please provide valid input for Schedule Type");
        CommonMethods.validateInputWithEnum(SoilTypes.class, newPlotRequest.getSoilType()
                , "Please provide valid input for Soil Type");
        CommonMethods.validateInputWithEnum(CropTypes.class, newPlotRequest.getCropName()
                , "Please provide valid input for Crop Name");
    }
}