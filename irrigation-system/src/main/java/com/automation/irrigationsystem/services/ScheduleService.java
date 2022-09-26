package com.automation.irrigationsystem.services;

import com.automation.irrigationsystem.common.CommonMethods;
import com.automation.irrigationsystem.common.IrrigationDays;
import com.automation.irrigationsystem.common.ResponseCodes;
import com.automation.irrigationsystem.common.ScheduleTypes;
import com.automation.irrigationsystem.entity.CustomSchedule;
import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.exception.DataNotFoundException;
import com.automation.irrigationsystem.exception.InvalidRequestException;
import com.automation.irrigationsystem.iface.IScheduleService;
import com.automation.irrigationsystem.repository.CustomScheduleRepository;
import com.automation.irrigationsystem.repository.PlotRepository;
import com.automation.irrigationsystem.request.ScheduleRequest;
import com.automation.irrigationsystem.response.NewScheduleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private CustomScheduleRepository customScheduleRepository;

    @Value("${irrigation.schedule.defaultStartTime}")
    String defaultStartTime;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public NewScheduleResponse addNewCustomSchedule(String customerId, String plotId, ScheduleRequest newScheduleRequest) {

        validateAddNewCustomScheduleReq(customerId, plotId, newScheduleRequest);
        Plot plot = plotRepository.findByCustomerIdAndIdAndActiveFlg(new BigDecimal(customerId)
                , new BigDecimal(plotId), BigDecimal.ONE);
        if(null == plot) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the customer_id and plot_id combination.");
        }

        return convertCustomSchedToResponseObj(buildCustomSchedObj(plot, newScheduleRequest), plotId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateCustomSchedule(String customerId, String plotId, String scheduleId, ScheduleRequest editScheduleRequest) {
        validateUpdateSchedueReq(customerId, plotId, scheduleId, editScheduleRequest);

        CustomSchedule customSchedule = customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                new BigDecimal(scheduleId), new BigDecimal(customerId), BigDecimal.ONE);

        if(null == customSchedule) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Schedule not found for the schedId, customerId combination.");
        }

        Plot plot = plotRepository.findByIdAndCustomerIdAndCustomSchedIdAndActiveFlg(
                new BigDecimal(plotId) ,new BigDecimal(customerId)
                , new BigDecimal(scheduleId), BigDecimal.ONE);

        if(null == plot) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the schedId, customerId, plotId combination.");
        }
        buildAndUpdateCustomSched(editScheduleRequest, customSchedule);
        log.info("<<<<<<<< Schedule has been updated successfully");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCustomSchedule(String customerId, String plotId, String scheduleId) {
        validateDeleteSchedReq(customerId, plotId, scheduleId);
        CustomSchedule customSchedule = customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                new BigDecimal(scheduleId), new BigDecimal(customerId), BigDecimal.ONE);

        if(null == customSchedule) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Schedule not found for the schedId, customerId combination.");
        }

        List<Plot> plotList = plotRepository.findByCustomerIdAndCustomSchedIdAndActiveFlg(
                new BigDecimal(customerId)
                , new BigDecimal(scheduleId), BigDecimal.ONE);

        if(CollectionUtils.isEmpty(plotList)) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the schedId, customerId combination.");
        }

        Optional<Plot> plotOp = plotList.stream().filter(e->e.getId().compareTo(new BigDecimal(plotId)) == 0).findFirst();

        if(!plotOp.isPresent()) {
            throw new DataNotFoundException(ResponseCodes.DATA_NOT_FOUND, "Plot not found for the schedId, customerId, plotId combination.");
        }

        Plot plot = plotOp.get();
        plot.setCustomSchedId(null);
        plot.setModifiedDt(new Date());
        plotRepository.save(plot);

        if(plotList.size() == 1) {
            customSchedule.setActiveFlg(BigDecimal.ZERO);
            customSchedule.setModifiedDt(new Date());
            customScheduleRepository.save(customSchedule);
        }
        log.info("<<<<<<<< Schedule has been deleted successfully");
    }

    private void validateDeleteSchedReq(String customerId, String plotId, String scheduleId) {
        CommonMethods.throwExceptionIfEmpty(scheduleId,
                "schedule Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(scheduleId,
                "Please input valid schedule id.");

        CommonMethods.throwExceptionIfEmpty(customerId,
                "Customer Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(customerId,
                "Please input valid customer id.");

        CommonMethods.throwExceptionIfEmpty(plotId,
                "Plot Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(plotId,
                "Please input valid plot id.");

    }

    private void buildAndUpdateCustomSched(ScheduleRequest req, CustomSchedule customSchedule) {

        customSchedule.setScheduleName(CommonMethods.getDefaultValueIfNull(customSchedule.getScheduleName(), req.getScheduleName()));
        customSchedule.setDescription(CommonMethods.getDefaultValueIfNull(customSchedule.getDescription(), req.getDescription()));
        customSchedule.setIrrigationDay((CommonMethods.getDefaultValueIfNull(customSchedule.getIrrigationDay(), req.getIrrigationDay())).trim().toUpperCase());
        customSchedule.setHoursToIrrigate(CommonMethods.getDefaultValueBDIfNull(customSchedule.getHoursToIrrigate(), req.getHoursToIrrigate()));
        String startTime = CommonMethods.getTruncatedStartTime(CommonMethods.getDefaultValueIfNull(customSchedule.getStartTime(), req.getStartTime()), defaultStartTime);
        customSchedule.setStartTime(startTime);
        customSchedule.setModifiedDt(new Date());
        customScheduleRepository.save(customSchedule);
    }

    private void validateUpdateSchedueReq(String customerId, String plotId, String scheduleId, ScheduleRequest editScheduleRequest) {
        CommonMethods.throwExceptionIfEmpty(scheduleId,
                "schedule Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(scheduleId,
                "Please input valid schedule id.");

        commonScheduleValidation(customerId, plotId, editScheduleRequest);
    }

    private NewScheduleResponse convertCustomSchedToResponseObj(CustomSchedule customSchedule, String plotId) {
        NewScheduleResponse resp = new NewScheduleResponse();
        resp.setScheduleId(customSchedule.getId().toString());
        resp.setScheduleName(customSchedule.getScheduleName());
        resp.setDescription(customSchedule.getDescription());
        resp.setIrrigationDay(customSchedule.getIrrigationDay());
        resp.setHoursToIrrigate(customSchedule.getHoursToIrrigate().toString());
        resp.setPlotId(plotId);
        resp.setCustomerId(customSchedule.getCustomerId().toString());
        resp.setCreatedDt(customSchedule.getCreatedDt());
        resp.setResponseCode(ResponseCodes.SUCCESS);
        resp.setResponseMessage(ResponseCodes.CREATED_MSG);
        resp.setResponseTimeStamp(new Date());
        log.info("<<<<<<< Schedule has been added successfully");
        return resp;
    }

    private CustomSchedule buildCustomSchedObj(Plot plot, ScheduleRequest newScheduleRequest) {
        CustomSchedule customSchedule = new CustomSchedule();
        customSchedule.setCustomerId(plot.getCustomerId());
        customSchedule.setScheduleName(newScheduleRequest.getScheduleName().trim().toUpperCase());
        customSchedule.setDescription(newScheduleRequest.getDescription().trim());
        customSchedule.setIrrigationDay(newScheduleRequest.getIrrigationDay().trim().toUpperCase());
        customSchedule.setStartTime(CommonMethods.getTruncatedStartTime(newScheduleRequest.getStartTime(), defaultStartTime));
        customSchedule.setHoursToIrrigate(new BigDecimal(newScheduleRequest.getHoursToIrrigate().trim()));
        customSchedule.setActiveFlg(BigDecimal.ONE);
        customSchedule.setCreatedDt(new Date());
        customSchedule = customScheduleRepository.save(customSchedule);

        plot.setCustomSchedId(customSchedule.getId());
        plot.setModifiedDt(new Date());

        if(plot.getScheduleType().equals(ScheduleTypes.REGULAR.name())) {
            plot.setScheduleType(ScheduleTypes.CUSTOM.name());
            log.info("Changing schedule from REGULAR to CUSTOM for the plot id: {}", plot.getId());
        }

        plotRepository.save(plot);
        return customSchedule;
    }

    private void validateAddNewCustomScheduleReq(String customerId, String plotId, ScheduleRequest newScheduleRequest) {
        CommonMethods.throwExceptionIfEmpty(newScheduleRequest.getScheduleName(),
                "Schedule name must not be empty");
        CommonMethods.throwExceptionIfEmpty(newScheduleRequest.getDescription(),
                "schedule Description Id must not be empty");
        CommonMethods.throwExceptionIfEmpty(newScheduleRequest.getIrrigationDay(),
                "Schedule Irrigation day must not be empty");
        CommonMethods.throwExceptionIfEmpty(newScheduleRequest.getHoursToIrrigate(),
                "Schedule hours to irrigate must not be empty");

        commonScheduleValidation(customerId, plotId, newScheduleRequest);

    }

    private void commonScheduleValidation(String customerId, String plotId, ScheduleRequest scheduleRequest) {
        CommonMethods.throwExceptionIfEmpty(customerId,
                "Customer Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(customerId,
                "Please input valid customer id.");

        CommonMethods.throwExceptionIfEmpty(plotId,
                "Plot Id must not be empty");
        CommonMethods.throwExceptionIfNotNumber(plotId,
                "Please input valid plot id.");

        CommonMethods.throwExceptionIfLengthInvalid(scheduleRequest.getScheduleName(), 50,
                "Schedule name should not be more than 50 character long");
        CommonMethods.throwExceptionIfLengthInvalid(scheduleRequest.getDescription(), 200,
                "Schedule description should not be more than 200 character long");
        CommonMethods.throwExceptionIfLengthInvalid(scheduleRequest.getIrrigationDay(), 20,
                "Schedule irrigation day should not be more than 20 character long");

        CommonMethods.throwExceptionIfNotNumber(scheduleRequest.getHoursToIrrigate(),
                "Please provide valid input for schedule hours to irrigate");

        CommonMethods.validateInputWithEnum(IrrigationDays.class, scheduleRequest.getIrrigationDay()
                , "Please provide valid input for Irrigation days");

        if(!CommonMethods.validateIrrigationStartTime(scheduleRequest.getStartTime())){
            throw new InvalidRequestException(ResponseCodes.INVALID_INPUT, "Irrigation start time must be in HH24:MM format.");
        }
    }
}
