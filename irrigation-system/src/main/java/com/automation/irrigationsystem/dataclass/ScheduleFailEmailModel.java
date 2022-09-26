package com.automation.irrigationsystem.dataclass;

import lombok.Data;

@Data
public class ScheduleFailEmailModel {

    private String emailFrom;
    private String emailTo;
    private String emailSubject;
    private String emailBody;
    private String emailPdw;

}
