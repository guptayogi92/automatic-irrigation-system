package com.automation.irrigationsystem.config;

import com.automation.irrigationsystem.common.SchedulingState;
import com.automation.irrigationsystem.common.SendEmailUtility;
import com.automation.irrigationsystem.dataclass.ScheduleFailEmailModel;
import com.automation.irrigationsystem.entity.PlotIrrigationState;
import com.automation.irrigationsystem.exception.SensorNotAvailableException;
import com.automation.irrigationsystem.iface.IDeviceCommunicationService;
import com.automation.irrigationsystem.repository.PlotIrrigationStateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DoIrrigationScheduler {

    @Autowired
    private PlotIrrigationStateRepository plotIrrigationStateRepository;

    @Autowired
    private IDeviceCommunicationService iDeviceCommunicationService;

    @Value("${irrigation.schedule.retryCount:3}")
    private int retryCount;

    @Value("${irrigation.alert.deviceCommFail.emailFrom:gupta.yogesh1999@gmail.com}")
    private String emailFrom;

    @Value("${irrigation.alert.deviceCommFail.emailTo:gupta.yogi92@gmail.com}")
    private String emailTo;

    @Value("${irrigation.alert.deviceCommFail.emailSub}")
    private String emailSubject;

    @Value("${irrigation.alert.deviceCommFail.emailBody}")
    private String emailBody;

    @Value("${irrigation.alert.deviceCommFail.emailPdw}")
    private String emailPdw;

    @Scheduled(fixedDelay = 100000)
    public void doTheIrrigation() {
        log.info("---Scheduler start to do the irrigation, Configured Retry Count is: {}", retryCount);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar retryStartDate = Calendar.getInstance();

        startDate.add(Calendar.MINUTE, -21);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.add(Calendar.MINUTE, 6);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        retryStartDate.add(Calendar.DATE, -1);
        retryStartDate.set(Calendar.HOUR_OF_DAY, 23);
        retryStartDate.set(Calendar.MINUTE, 0);
        retryStartDate.set(Calendar.SECOND, 0);
        retryStartDate.set(Calendar.MILLISECOND, 0);

        List<PlotIrrigationState> plotsToIrrigate = plotIrrigationStateRepository.getPlotListToIrrigate(
                SchedulingState.CONFIGURED.name(), startDate.getTime(), endDate.getTime()
                , SchedulingState.RETRY.name(), retryStartDate.getTime());

        if(CollectionUtils.isEmpty(plotsToIrrigate)) {
            log.info("There is no plots to irrigate.");
            return;
        }
        log.info("Plot count to irrigate is: {}", plotsToIrrigate.size());
        plotsToIrrigate.forEach(obj ->
                CompletableFuture.runAsync(() -> scheduleIrrigation(obj)));
    }

    private void scheduleIrrigation(PlotIrrigationState obj) {
        if(null != obj.getRetryCount() && obj.getRetryCount().intValue() >= retryCount) {
            createSendEmailModel(obj.getPlotId().toString(), obj.getDeviceId().toString(), "failed to Irrigate");
            changePlotIrrState(SchedulingState.FAILED.name(), obj.getRetryCount(), obj);
            return;
        }
        BigDecimal nextIrriCount = null == obj.getRetryCount() ? BigDecimal.ONE :
                obj.getRetryCount().add(BigDecimal.ONE);
        try {
            changePlotIrrState(SchedulingState.IN_PROGRESS.name(), nextIrriCount, obj);
            iDeviceCommunicationService.startCommunication(obj.getDeviceId(), obj.getHoursToIrrigate().intValue(), obj.getPlotId());
        }catch(SensorNotAvailableException e) {
            log.debug("Sensor device throw an exception in irrigating the plot: '{}', timestamp: '{}'",e.getPlotId(), e.getTimeStamp());
            if(nextIrriCount.intValue() >= retryCount) {
                changePlotIrrState(SchedulingState.FAILED.name()
                        , nextIrriCount, obj);
                createSendEmailModel(obj.getPlotId().toString(), obj.getDeviceId().toString(), e.getMessage());
            }else {
                changePlotIrrState(SchedulingState.RETRY.name(), nextIrriCount, obj);
            }
            return;
        }
        changePlotIrrState(SchedulingState.COMPLETED.name(), nextIrriCount, obj);
    }

    private void changePlotIrrState(String newIrrState, BigDecimal retryCount, PlotIrrigationState obj) {
        obj.setIrrigationState(newIrrState);
        obj.setRetryCount(retryCount);
        obj.setModifiedDt(new Date());
        plotIrrigationStateRepository.save(obj);

    }

    private void createSendEmailModel(String plotId, String deviceId, String reasonText) {
        log.info("Triggering the email alert plotId: {}, deviceId {}", plotId, deviceId);

        String formattedSub = emailSubject.replace("$plot_id", plotId);
        String formattedBody = emailBody.replace("$plot_id", plotId).replace("$reason_text", reasonText);
        ScheduleFailEmailModel model = new ScheduleFailEmailModel();
        model.setEmailSubject(formattedSub);
        model.setEmailBody(formattedBody);
        model.setEmailFrom(emailFrom);
        model.setEmailTo(emailTo);
        model.setEmailPdw(emailPdw);


        SendEmailUtility email = new SendEmailUtility();
        email.triggerEmail(model);
    }
}