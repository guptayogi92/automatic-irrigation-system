package com.automation.irrigationsystem.config;

import com.automation.irrigationsystem.common.CommonMethods;
import com.automation.irrigationsystem.common.IrrigationDays;
import com.automation.irrigationsystem.common.ScheduleTypes;
import com.automation.irrigationsystem.common.SchedulingState;
import com.automation.irrigationsystem.dataclass.PlotScheduleModel;
import com.automation.irrigationsystem.entity.CustomSchedule;
import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.entity.PlotIrrigationState;
import com.automation.irrigationsystem.entity.Schedule;
import com.automation.irrigationsystem.repository.CustomScheduleRepository;
import com.automation.irrigationsystem.repository.PlotIrrigationStateRepository;
import com.automation.irrigationsystem.repository.PlotRepository;
import com.automation.irrigationsystem.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConfigureIrrigationScheduler {

    @Autowired private PlotRepository plotRepository;

    @Autowired private CustomScheduleRepository customScheduleRepository;

    @Autowired private ScheduleRepository scheduleRepository;

    @Autowired private PlotIrrigationStateRepository plotIrrigationStateRepository;

    @Value("${irrigation.schedule.defaultStartTime}")
    String defaultStartTime;

    @Scheduled(cron = "0 0 0 * * *")
    public void oneTimeSchedulerADay() {
        log.info("--Daily Scheduler start to configure PLOT_IRRIGATION_STATE table");
        Calendar now = Calendar.getInstance();
        String weatherType = CommonMethods.getWeatherNameForDate(now);
        List<String> irrigationDaysList = getIrrigationDaysList(now);
        List<PlotScheduleModel> plotSchedModel = getPlotSchedModel(weatherType, irrigationDaysList);
        configurePlotIrrigationState(plotSchedModel);
    }

    private void configurePlotIrrigationState(List<PlotScheduleModel> plotSchedModel) {
        if(CollectionUtils.isEmpty(plotSchedModel)) {
            log.info("No Plots found to configure in PLOT_IRRIGATION_TABLE by the scheduler");
            return;
        }
        log.info("Records to configure by daily scheduler count is: {}", plotSchedModel.size());
        plotSchedModel.forEach(e -> {
            PlotIrrigationState plotIrrState = new PlotIrrigationState();
            plotIrrState.setPlotId(e.getPlotId());
            plotIrrState.setIrrigationState(SchedulingState.CONFIGURED.name());
            plotIrrState.setRetryCount(BigDecimal.ZERO);
            plotIrrState.setScheduledDt(getScheduledDt(e.getStartTime()));
            plotIrrState.setDeviceId(e.getDeviceId());
            plotIrrState.setHoursToIrrigate(e.getHoursToIrrigate());
            plotIrrState.setCreatedDt(new Date());
            plotIrrigationStateRepository.save(plotIrrState);
        });
    }

    private Date getScheduledDt(String startTime) {
        String truncStartTime = CommonMethods.getTruncatedStartTime(startTime, defaultStartTime);
        String [] splitStr = truncStartTime.split(":");
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitStr[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(splitStr[1]));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private List<PlotScheduleModel> getPlotSchedModel(String weatherType, List<String> irrigationDaysList) {

        Calendar todayStartDt = Calendar.getInstance();
        Calendar todayEndDt = Calendar.getInstance();

        todayStartDt.set(Calendar.HOUR_OF_DAY, 0);
        todayStartDt.set(Calendar.MINUTE, 0);
        todayStartDt.set(Calendar.SECOND, 0);
        todayStartDt.set(Calendar.MILLISECOND, 0);

        todayEndDt.set(Calendar.HOUR_OF_DAY, 0);
        todayEndDt.set(Calendar.MINUTE, 0);
        todayEndDt.set(Calendar.SECOND, 0);
        todayEndDt.set(Calendar.MILLISECOND, 0);
        todayEndDt.add(Calendar.DATE, 1);

        List<PlotIrrigationState> schedPlotForToday = plotIrrigationStateRepository.getPlotsForToday(todayStartDt.getTime(), todayEndDt.getTime());
        List<BigDecimal> configuredPlotIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(schedPlotForToday)){
            configuredPlotIds = schedPlotForToday.stream().map(PlotIrrigationState::getPlotId).toList();
        }
        List<PlotScheduleModel> model = new ArrayList<>();
        List<PlotScheduleModel> customSchedModel = getCustomSchedModel(irrigationDaysList, configuredPlotIds);
        List<PlotScheduleModel> regSchedModel = getRegularSchedModel(weatherType, irrigationDaysList, configuredPlotIds);
        if(!CollectionUtils.isEmpty(customSchedModel)) {
            model.addAll(customSchedModel);
        }
        if(!CollectionUtils.isEmpty(regSchedModel)) {
            model.addAll(regSchedModel);
        }

        return model;
    }

    private List<PlotScheduleModel> getRegularSchedModel(String weatherType
            , List<String> irrigationDaysList, List<BigDecimal> configuredPlotIds) {
        List<Schedule> regSchedList = scheduleRepository.findByWeatherTypeAndIrrigationDayInAndActiveFlg(
                weatherType, irrigationDaysList, BigDecimal.ONE);
        if(regSchedList.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Schedule> regSchedMap = new HashMap<>();
        Set<String> cropNameSet = new HashSet<>();
        Set<String> soilTypeSet = new HashSet<>();

        regSchedList.forEach(e -> {
            cropNameSet.add(e.getCropName());
            soilTypeSet.add(e.getSoilType());
            regSchedMap.put(e.getCropName()+"_"+e.getSoilType(), e);
        });

        List<Plot> plotListLocal = plotRepository.findByScheduleTypeAndCropNameInAndSoilTypeInAndActiveFlg(
                ScheduleTypes.REGULAR.name(), cropNameSet, soilTypeSet, BigDecimal.ONE);
        if(plotListLocal.isEmpty()) {
            return Collections.emptyList();
        }
        List<PlotScheduleModel> model = new ArrayList<>();

        plotListLocal.forEach(e -> {
            if(configuredPlotIds.contains(e.getId())) {
                return;
            }
            PlotScheduleModel obj = new PlotScheduleModel();
            Schedule cs = regSchedMap.get(e.getCropName()+"_"+e.getSoilType());
            obj.setPlotId(e.getId());
            obj.setPlotName(e.getPlotName());
            obj.setCustomerId(e.getCustomerId());
            obj.setPlotDescription(e.getDescription());
            obj.setAddress(e.getAddress());
            obj.setWidth(e.getWidth());
            obj.setHeight(e.getHeight());
            obj.setUnit(e.getUnit());
            obj.setScheduleType(e.getScheduleType());
            obj.setCustomSchedId(e.getCustomSchedId());
            obj.setDeviceId(e.getDeviceId());
            obj.setCropName(e.getCropName());
            obj.setSoilType(e.getSoilType());

            obj.setScheduleName(cs.getScheduleName());
            obj.setScheduleDescription(cs.getDescription());
            obj.setIrrigationDay(cs.getIrrigationDay());
            obj.setHoursToIrrigate(cs.getHoursToIrrigate());
            obj.setStartTime(defaultStartTime);
            model.add(obj);
        });
        return model;
    }

    private List<PlotScheduleModel> getCustomSchedModel(List<String> irrigationDaysList, List<BigDecimal> configuredPlotIds) {
        List<CustomSchedule> customSchedList = customScheduleRepository.findByIrrigationDayInAndActiveFlg(
                irrigationDaysList, BigDecimal.ONE);
        if(customSchedList.isEmpty()) {
           return Collections.emptyList();
        }
        Map<BigDecimal, CustomSchedule> customSchedMap = customSchedList.stream()
                .collect(Collectors.toMap(CustomSchedule::getId, Function.identity()));
        List<Plot> plotListLocal = plotRepository.findByScheduleTypeAndCustomSchedIdInAndActiveFlg(
                ScheduleTypes.CUSTOM.name(), customSchedMap.keySet(), BigDecimal.ONE);
        if(CollectionUtils.isEmpty(plotListLocal)) {
            return Collections.emptyList();
        }
        List<PlotScheduleModel> model = new ArrayList<>();

        plotListLocal.forEach(e -> {
            if(configuredPlotIds.contains(e.getId())) {
                return;
            }
            PlotScheduleModel obj = new PlotScheduleModel();
            CustomSchedule cs = customSchedMap.get(e.getCustomSchedId());
            obj.setPlotId(e.getId());
            obj.setPlotName(e.getPlotName());
            obj.setCustomerId(e.getCustomerId());
            obj.setPlotDescription(e.getDescription());
            obj.setAddress(e.getAddress());
            obj.setWidth(e.getWidth());
            obj.setHeight(e.getHeight());
            obj.setUnit(e.getUnit());
            obj.setScheduleType(e.getScheduleType());
            obj.setCustomSchedId(e.getCustomSchedId());
            obj.setDeviceId(e.getDeviceId());
            obj.setCropName(e.getCropName());
            obj.setSoilType(e.getSoilType());

            obj.setScheduleName(cs.getScheduleName());
            obj.setScheduleDescription(cs.getDescription());
            obj.setIrrigationDay(cs.getIrrigationDay());
            obj.setHoursToIrrigate(cs.getHoursToIrrigate());
            obj.setStartTime(cs.getStartTime());

            model.add(obj);
        });
        return model;

    }

    private List<String> getIrrigationDaysList(Calendar now) {
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK); //1: Sunday, 7: Saturday
        List<String> irrigationDaysList = new ArrayList<>();
        irrigationDaysList.add(IrrigationDays.DAILY.name());
        if(dayOfWeek == 2 || dayOfWeek == 5) {
            irrigationDaysList.add(IrrigationDays.BIWEEKLY.name());
        }else if(dayOfWeek == 4) {
            irrigationDaysList.add(IrrigationDays.WEEKLY.name());
        }
        return irrigationDaysList;
    }

}
